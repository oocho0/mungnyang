package com.mungnyang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAuthResponseDto {
    private String code;
    private String error;
    private String error_description;
    private String state;
}
