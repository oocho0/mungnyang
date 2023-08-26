package com.mungnyang.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("kakao-key")
public final class KakaoKey {

    private final String clientId;
    private final String clientSecret;
    private final String admin;
}
