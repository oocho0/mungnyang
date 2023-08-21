package com.mungnyang.service;

import com.mungnyang.constant.Url;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    private MemberDto testDto;

    @BeforeEach
    void createTestMember(){
        MemberDto memberDto = new MemberDto();
        memberDto.setName("test1");
        memberDto.setEmail("abc@abc.com");
        memberDto.setPassword("12345678");
        memberDto.setZipcode("1234");
        memberDto.setAddress("서울");
        memberDto.setTel("010-0000-0000");
        memberDto.setRole("admin");
        memberService.saveMember(memberDto);
        testDto = memberDto;
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void signUp() {
        //given - @BeforeEach
        //when
        Member savedMember = memberService.findMember(testDto.getEmail());
        //then
        assertThat(savedMember.getName()).isEqualTo(testDto.getName());
        assertThat(savedMember.getTel()).isEqualTo(testDto.getTel());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    void duplicate() {
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setName("test1");
        memberDto.setEmail("abc@abc.com");
        memberDto.setPassword("12345678");
        memberDto.setAddress("seoul");
        memberDto.setTel("010-0000-0000");
        memberDto.setRole("admin");

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(memberDto);
        });

        //then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 회원 입니다.");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl(Url.LOGIN).user(testDto.getEmail()).password(testDto.getPassword()))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFail() throws Exception {
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl(Url.LOGIN).user(testDto.getEmail()).password("87654321"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}