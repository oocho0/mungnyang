package com.mungnyang.service;

import com.mungnyang.constant.Role;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.entity.Member;
import com.mungnyang.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 테스트")
    void signUp(){
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setName("test1");
        memberDto.setEmail("abc@abc.com");
        memberDto.setPassword("12345678");
        memberDto.setAddress("seoul");
        memberDto.setTel("010-0000-0000");
        memberDto.setRole(Role.ADMIN);
        memberService.saveMember(memberDto);
        //when
        Member savedMember = memberService.findMember(memberDto.getEmail());
        //then
        assertThat(savedMember.getName()).isEqualTo(memberDto.getName());
        assertThat(savedMember.getTel()).isEqualTo(memberDto.getTel());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    void duplicate(){
        //given
        MemberDto memberDtoA = new MemberDto();
        memberDtoA.setName("test1");
        memberDtoA.setEmail("abc@abc.com");
        memberDtoA.setPassword("12345678");
        memberDtoA.setAddress("seoul");
        memberDtoA.setTel("010-0000-0000");
        memberDtoA.setRole(Role.ADMIN);
        memberService.saveMember(memberDtoA);

        MemberDto memberDtoB = new MemberDto();
        memberDtoB.setName("test1");
        memberDtoB.setEmail("abc@abc.com");
        memberDtoB.setPassword("12345678");
        memberDtoB.setAddress("seoul");
        memberDtoB.setTel("010-0000-0000");
        memberDtoB.setRole(Role.ADMIN);

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->{memberService.saveMember(memberDtoB);});

        //then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 회원 입니다.");
    }
}