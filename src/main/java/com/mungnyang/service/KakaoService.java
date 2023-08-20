package com.mungnyang.service;

import com.mungnyang.dto.KakaoAuthResponseDto;
import com.mungnyang.dto.KakaoTokenRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;


    public ResponseEntity<KakaoAuthResponseDto> authRequest(String auth) {
        String requestUrl = "https://kauth.kakao.com/oauth/authorize";
        String clientId = "520f52e3ed17c067c41286fef8b4bcc2";
        String redirectUri = "/member/kakao-token";
        URI uri = UriComponentsBuilder.fromUriString(requestUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", auth)
                .encode().build().toUri();
        return restTemplate.getForEntity(uri, KakaoAuthResponseDto.class);
    }

    public ResponseEntity<?> tokenRequest(String code, String csrf){
        String requestUrl = "https://kauth.kakao.com/oauth/token";
        String grantType = "authorization_code";
        String clientId = "520f52e3ed17c067c41286fef8b4bcc2";
        String redirectUri = "/member";

        URI uri = UriComponentsBuilder.fromUriString(requestUrl)
                .encode().build().toUri();
        KakaoTokenRequestDto requestDto = new KakaoTokenRequestDto(grantType, clientId, redirectUri, code);
        RequestEntity<KakaoTokenRequestDto> requestEntity = RequestEntity.post(requestUrl)
                .contentType(new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8))
                .body(requestDto);
        return restTemplate.postForEntity(uri, requestEntity, )
    }
}
