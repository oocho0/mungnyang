package com.mungnyang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungnyang.constant.Kakao;
import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.KakaoInfoDto;
import com.mungnyang.dto.KakaoMemberDto;
import com.mungnyang.dto.KakaoTokenResponseDto;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@Transactional
public class KakaoService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MemberService memberService;

    /**
     * 카카오 로그인 REST API에 인가 요청할 요청URI 만들기
     * @param csrf
     * @return 인가요청URI
     */
    public String getKakaoAuthRequestURI(String csrf) {

        return UriComponentsBuilder.fromUriString(Kakao.AUTH_REQUEST_URL)
                .queryParam("client_id", Kakao.CLIENT_ID)
                .queryParam("redirect_uri", Url.FROM_URL+Url.KAKAO_LOGIN_REDIRECT_URI)
                .queryParam("response_type", "code")
                .queryParam("state", csrf)
                .queryParam("scope", "openid")
                .queryParam("nonce", Kakao.NONCE)
                .encode().build().toUriString();
    }

    /**
     * 카카오 로그인 REST API에 토근 요청 하기
     * @param code 카카오로부터 인가 받은 회원 code
     * @return 카카오 토큰 응답 객체 KakaoTeokenResponseDto와 응답 코드
     */
    public ResponseEntity<KakaoTokenResponseDto> tokenRequest(String code){
        String grantType = "authorization_code";
        String redirectUri = Url.FROM_URL + Url.KAKAO_LOGIN_REDIRECT_URI;

        URI uri = UriComponentsBuilder.fromUriString(Kakao.TOKEN_REQUEST_URL)
                .encode().build().toUri();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", Kakao.CLIENT_ID);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", Kakao.CLIENT_SECRET);
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
                .contentType(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8))
                .body(params);
        return restTemplate.exchange(requestEntity, KakaoTokenResponseDto.class);
    }

    /**
     * 카카오 토큰 응답으로부터 카카오 회원 정보 얻기
     * @param response 카카오 토근 응답 객체 KakaoTokenResponseDto
     * @return 카카오 회원 정보 객체 KakaoInfoDto
     */
    public KakaoInfoDto getLoginInfo(ResponseEntity<KakaoTokenResponseDto> response) {
        String openIdToken = response.getBody().getId_token();
        String[] openIds = openIdToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String openIdPayload = new String(decoder.decode(openIds[1]));
        KakaoInfoDto kakaoInfoDto;
        try {
             kakaoInfoDto = objectMapper.readValue(openIdPayload, KakaoInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(!kakaoInfoDto.getNonce().equals(Kakao.NONCE)){
            throw new IllegalStateException();
        }
        return kakaoInfoDto;
    }

    /**
     * 해당 카카오 계정이 회원이면 회원의 비밀번호를 반환, 아니면 null 반환
     * @param kakaoEmail 저장되는 카카오 회원 아이디 형식
     * @return 카카오 계정의 일련번호가 비밀번호
     */
    public String getMemberPW(String kakaoEmail) {
        Member member = memberService.findMember(kakaoEmail);
        if (member.getName().equals(Member.ANONYMOUS.getName())) {
            return Member.ANONYMOUS.getPassword();
        }
        return member.getEmail().split("@")[0];
    }

    /**
     * 카카오 계정으로 로그인 한 회원을 회원 DB에 저장하기
     * @param kakaoMemberDto 페이지에 입력된 회원 정보
     */
    public void saveKakaoMember(KakaoMemberDto kakaoMemberDto){
        String password = kakaoMemberDto.getEmail().split("@")[0];
        MemberDto newMemberDto = modelMapper.map(kakaoMemberDto, MemberDto.class);
        newMemberDto.setMemberType(MemberType.KAKAO);
        newMemberDto.setPassword(password);
        newMemberDto.setRole("user");
        memberService.saveMember(newMemberDto);
    }

    /**
     * 카카오 계정으로 로그인한 회원을 우리 서비스에 로그인 처리
     * @param request
     * @param kakaoEmail
     * @param kakaoPW
     */
    public void loginWithKakao(HttpServletRequest request, String kakaoEmail, String kakaoPW){
        Authentication auth = new UsernamePasswordAuthenticationToken(kakaoEmail, kakaoPW);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

    /**
     * 카카오 계정으로 최초 로그인한 회원을 우리 서비스에 로그인 처리
     * @param request
     * @param kakaoMemberDto
     */
    public void loginWithKakao(HttpServletRequest request, KakaoMemberDto kakaoMemberDto){
        String kakaoEmail = kakaoMemberDto.getEmail();
        String kakaoPW = getMemberPW(kakaoEmail);
        Authentication auth = new UsernamePasswordAuthenticationToken(kakaoEmail, kakaoPW);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

    /**
     * 카카오 로그아웃 REST API로 요청할 URI 만들기
     * @return 로그아웃요청URI
     */
    public String getKakaologoutRequestURI() {

        return UriComponentsBuilder.fromUriString(Kakao.LOGOUT_URL)
                .queryParam("client_id", Kakao.CLIENT_ID)
                .queryParam("logout_redirect_uri", Url.FROM_URL+Url.LOGOUT)
                .encode().build().toUriString();
    }
}
