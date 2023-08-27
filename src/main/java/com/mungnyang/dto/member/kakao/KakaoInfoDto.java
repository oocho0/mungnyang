package com.mungnyang.dto.member.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoInfoDto {
    private String iss;
    private String aud;
    private String sub;
    private Integer iat;
    private Integer exp;
    private Integer auth_time;
    private String nonce;
    private String nickname;
    private String email;
}
