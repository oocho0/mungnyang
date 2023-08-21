package com.mungnyang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungnyang.constant.Kakao;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.KakaoAuthResponseDto;
import com.mungnyang.dto.KakaoInfoDto;
import com.mungnyang.dto.KakaoTokenRequestDto;
import com.mungnyang.dto.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ResponseEntity<KakaoAuthResponseDto> authRequest(String csrf) {

        URI uri = UriComponentsBuilder.fromUriString(Kakao.AUTH_REQUEST_URL)
                .queryParam("client_id", Kakao.CLIENT_ID)
                .queryParam("redirect_uri", Url.KAKAO_REDIRECT_URI)
                .queryParam("response_type", "code")
                .queryParam("state", csrf)
                .queryParam("scope", "openid")
                .queryParam("nonce", Kakao.NONCE)
                .encode().build().toUri();
        return restTemplate.getForEntity(uri, KakaoAuthResponseDto.class);
    }

    public ResponseEntity<KakaoTokenResponseDto> tokenRequest(String code){
        String grantType = "authorization_code";

        URI uri = UriComponentsBuilder.fromUriString(requestUrl)
                .encode().build().toUri();
        KakaoTokenRequestDto requestDto = new KakaoTokenRequestDto(grantType, Kakao.CLIENT_ID, Url.KAKAO_REDIRECT_URI, code, Kakao.CLIENT_SECRET);
        RequestEntity<KakaoTokenRequestDto> requestEntity = RequestEntity.post(uri)
                .contentType(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8))
                .body(requestDto);
        return restTemplate.exchange(requestEntity, KakaoTokenResponseDto.class);
    }

    public KakaoInfoDto getLoginInfo(ResponseEntity<KakaoTokenResponseDto> response) {
        String openIdToken = response.getBody().getId_token();
        String[] openIds = openIdToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String openIdHeader = new String(decoder.decode(openIds[0]));
        String openIdPayload = new String(decoder.decode(openIds[1]));
        String openIdSignature = new String(decoder.decode(openIds[2]));
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

    public ResponseEntity<String> loginWithKakao(String csrf, String kakaoEmail){
        URI uri = UriComponentsBuilder.fromUriString(Url.FROM_URL).path(Url.LOGIN).encode().build().toUri();
        String body = "_csrf.parameterName="+csrf+"&email="+kakaoEmail+"&password="+"12345678";
        RequestEntity<String> requestEntity = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body);
        return restTemplate.exchange(requestEntity, String.class);
    }

    public void signUpWithKakao(String csrf, KakaoInfoDto kakaoInfoDto) {

    }
}
