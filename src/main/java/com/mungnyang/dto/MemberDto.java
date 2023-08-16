package com.mungnyang.dto;

import com.mungnyang.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberDto {

    private Long memberId;
    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    @Email(message = "이메일 형식으로 입력하세요.")
    @NotEmpty(message = "이메일을 입력하세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요.")
    private String password;
    private String encodedPassword;
    @NotBlank(message = "주소를 입력하세요.")
    private String address;
    @NotBlank(message = "전화 번호를 입력하세요.")
    @Length(min = 10, max = 11, message = "전화 번호가 제대로 입력되지 않았습니다. 다시 확인해주세요.")
    private String tel;
}
