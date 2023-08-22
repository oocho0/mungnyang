package com.mungnyang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungnyang.constant.Kakao;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.KakaoAuthResponseDto;
import com.mungnyang.dto.KakaoInfoDto;
import com.mungnyang.dto.KakaoTokenRequestDto;
import com.mungnyang.dto.KakaoTokenResponseDto;
import lombok.extern.slf4j.Slf4j;
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

    public String authRequest(String csrf) {

        return UriComponentsBuilder.fromUriString(Kakao.AUTH_REQUEST_URL)
                .queryParam("client_id", Kakao.CLIENT_ID)
                .queryParam("redirect_uri", Url.FROM_URL+Url.KAKAO_REDIRECT_URI)
                .queryParam("response_type", "code")
                .queryParam("state", csrf)
                .queryParam("scope", "openid")
                .queryParam("nonce", Kakao.NONCE)
                .encode().build().toUriString();
    }

    public ResponseEntity<KakaoTokenResponseDto> tokenRequest(String code){
        String grantType = "authorization_code";
        String redirectUri = Url.FROM_URL + Url.KAKAO_REDIRECT_URI;

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

    public void loginWithKakao(HttpServletRequest request, String kakaoEmail, String kakaoPW){
        Authentication auth = new UsernamePasswordAuthenticationToken(kakaoEmail, kakaoPW);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }
}
