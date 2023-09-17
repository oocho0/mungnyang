package com.mungnyang.constant;

import java.util.UUID;

public class Kakao {
    public static final String NONCE = UUID.randomUUID().toString();
    public static final String EMAIL = "@kakao.com";
    public static final String AUTH_REQUEST_URL = "https://kauth.kakao.com/oauth/authorize";
    public static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    public static final String LOGOUT_URL = "https://kauth.kakao.com/oauth/logout";

    public static final String WITHDRAW_URL = "https://kapi.kakao.com/v1/user/unlink";

}
