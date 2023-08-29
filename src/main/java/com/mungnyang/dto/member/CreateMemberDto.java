package com.mungnyang.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CreateMemberDto {

    private Long memberId;

    @NotBlank(message = "이름을 입력하지 않았습니다.")
    private String name;

    @Email(message = "이메일 형식으로 입력하세요.")
    @NotEmpty(message = "이메일을 입력하지 않았습니다.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력하지 않았습니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요.")
    private String password;

    @NotBlank(message = "우편번호를 입력하지 않았습니다.")
    private String addressZipcode;
    @NotBlank(message = "주소를 입력하지 않았습니다.")
    private String addressMain;
    private String addressDetail;
    private String addressExtra;

    @NotBlank(message = "전화번호를 입력하지 않았습니다.")
    @Length(min = 11, max = 13, message = "전화 번호가 올바르지 않습니다.")
    private String tel;

    private String role;
    private String memberType;
}
