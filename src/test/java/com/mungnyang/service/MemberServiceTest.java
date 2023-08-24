package com.mungnyang.service;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.member.MemberDto;
import com.mungnyang.dto.member.UpdateMemberDto;
import com.mungnyang.entity.member.Member;
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
    void 테스트에_사용할_객체를_먼저_리포지토리에_저장한_후에_testDto에_테스트_객체를_지정한다() {
        MemberDto memberDto = new MemberDto();
        memberDto.setName("test1");
        memberDto.setEmail("abc@abc.com");
        memberDto.setPassword("12345678");
        memberDto.setZipcode("1234");
        memberDto.setAddress("서울");
        memberDto.setTel("010-0000-0000");
        memberDto.setRole("admin");
        memberDto.setMemberType(MemberType.NORMAL);
        memberService.saveMember(memberDto);
        testDto = memberDto;
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void 먼저_저장된_테스트_객체가_잘_저장되어있는지_객체를_찾아_값을_확인한다() {
        //given - @BeforeEach
        //when
        Member savedMember = memberService.findMember(testDto.getEmail());
        //then
        assertThat(savedMember.getName()).isEqualTo(testDto.getName());
        assertThat(savedMember.getTel()).isEqualTo(testDto.getTel());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    void 먼저_저장되어있는_테스트_객체의_아이디와_중복되는_아이디를_저장하면_중복체크에서_예외가_발생한다() {
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setName("test1");
        memberDto.setEmail("abc@abc.com");
        memberDto.setPassword("12345678");
        memberDto.setAddress("seoul");
        memberDto.setTel("010-0000-0000");
        memberDto.setRole("admin");
        memberDto.setMemberType(MemberType.NORMAL);

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(memberDto);
        });

        //then
        assertThat(e.getMessage()).isEqualTo("이미 가입된 회원 입니다.");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void 먼저_저장된_테스트_객체의_아이디와_비밀번호로_로그인을_시도하여_인가되었음을_확인한다() throws Exception {
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl(Url.LOGIN).user(testDto.getEmail()).password(testDto.getPassword()))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void 먼저_저장된_테스트_객체의_아이디와_틀린_비밀번호로_로그인을_시도하여_인가에_실패한다() throws Exception {
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl(Url.LOGIN).user(testDto.getEmail()).password("87654321"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Test
    @DisplayName("회원 정보 로딩 테스트")
    void 먼저_저장된_테스트_객체를_찾아와_다시_MemberDto로_변환했을떄_올바르게_변환되었는지_값을_확인한다() {
        Member findMember = memberService.findMember(testDto.getEmail());
        UpdateMemberDto mappingMemberDto = memberService.of(findMember);
        assertThat(mappingMemberDto.getMemberType()).isEqualTo(testDto.getMemberType());
        assertThat(mappingMemberDto.getEmail()).isEqualTo(testDto.getEmail());
        assertThat(mappingMemberDto.getZipcode()).isEqualTo(testDto.getZipcode());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void 먼저_저장된_테스트_객체를_찾아와_다시_수정해서_올바르게_변환되었는지_값을_확인하고_변환되면_안되는_값이_변하지_않았는지_확인한다(){
        Member findMember = memberService.findMember(testDto.getEmail());
        UpdateMemberDto updateMemberDto = memberService.of(findMember);
        updateMemberDto.setEmail("test2@abc.com");
        updateMemberDto.setName("수정테스트");
        updateMemberDto.setZipcode("4321");
        memberService.updateMember(updateMemberDto, findMember);
        Member updatedMember = memberService.findMember(testDto.getEmail());
        assertThat(updatedMember.getEmail()).isEqualTo(testDto.getEmail());
        assertThat(updatedMember.getName()).isEqualTo("수정테스트");
        assertThat(updatedMember.getAddress().getZipcode()).isEqualTo("4321");
        assertThat(updatedMember.getAddress().getAddress()).isEqualTo(testDto.getAddress());
    }
}