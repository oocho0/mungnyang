package com.mungnyang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {

    private String currentPassword;
    private String newPassword;
}
