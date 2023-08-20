package com.mungnyang.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor()
public class KakaoTokenRequestDto {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;
    private String client_secret;

    public KakaoTokenRequestDto(String grant_type, String client_id, String redirect_uri, String code) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.code = code;
    }
}
