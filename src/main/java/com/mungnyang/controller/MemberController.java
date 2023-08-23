package com.mungnyang.controller;

import com.mungnyang.constant.Kakao;
import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.*;
import com.mungnyang.entity.Member;
import com.mungnyang.service.KakaoService;
import com.mungnyang.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/new")
    public String signUpMain() {
        return "member/signUpMain";
    }

    @GetMapping("/new/{role}")
    public String signUpAdmin(@PathVariable String role, Model model) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberType(MemberType.NORMAL);
        if (role.equals("admin")) {
            memberDto.setRole("admin");
        } else {
            memberDto.setRole("user");
        }
        model.addAttribute("memberDto", memberDto);
        return "member/signUp";
    }

    @PostMapping("/new/{role}")
    public String signUp(@ModelAttribute @Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/signUp";
        }
        try {
            memberService.saveMember(memberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signUp";
        }
        return "redirect:" + Url.LOGIN;
    }

    @GetMapping("/kakao-auth")
    public String requestAuth(@RequestParam String csrf){
        return "redirect:" + kakaoService.getKakaoAuthRequestURI(csrf);
    }

    @GetMapping("/kakao-token")
    public String requestToken(HttpServletRequest request, @ModelAttribute KakaoAuthResponseDto kakaoAuth, Model model){
        String code = kakaoAuth.getCode();
        ResponseEntity<KakaoTokenResponseDto> response = kakaoService.tokenRequest(code);
        KakaoInfoDto kakaoInfoDto = kakaoService.getLoginInfo(response);
        String kakaoEmail = kakaoInfoDto.getSub() + Kakao.EMAIL;
        String kakaoName = kakaoInfoDto.getNickname();
        String kakaoPW = kakaoService.getMemberPW(kakaoEmail);
        if(kakaoPW != null){
            kakaoService.loginWithKakao(request, kakaoEmail, kakaoPW);
            return "redirect:" + Url.MAIN;
        }
        KakaoMemberDto kakaoMemberDto = new KakaoMemberDto();
        kakaoMemberDto.setName(kakaoName);
        kakaoMemberDto.setEmail(kakaoEmail);
        model.addAttribute("KakaoMemberDto", kakaoMemberDto);
        return "member/kakaoInfo";
    }

    @PostMapping("/new-kakao")
    public String singUpWithKakao(HttpServletRequest request, @ModelAttribute @Valid KakaoMemberDto kakaoMemberDto,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/kakaoInfo";
        }
        try {
            kakaoService.saveKakaoMember(kakaoMemberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/kakaoInfo";
        }
        kakaoService.loginWithKakao(request, kakaoMemberDto);
        return "redirect:" + Url.MAIN;
    }


    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "member/login";
    }

    @GetMapping("/pre-logout")
    public String logout(Principal principal){
        Member SignInMember = memberService.findMember(principal.getName());
        if(SignInMember.getMemberType().equals(MemberType.KAKAO)){
            return "redirect:" + kakaoService.getKakaologoutRequestURI();
        }
        return "redirect:" + Url.LOGOUT;
    }

    @GetMapping("/myPage")
    public String getMemberInfo(Principal principal, Model model){
        Member signInMember = memberService.findMember(principal.getName());
        UpdateMemberDto updateMemberDto = memberService.of(signInMember);
        model.addAttribute("updateMemberDto", updateMemberDto);
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        model.addAttribute("updatePasswordDto", updatePasswordDto);
        return "member/myPage";
    }

    @PatchMapping("/modify")
    public String modifyInfo(@ModelAttribute @Valid UpdateMemberDto updateMemberDto, BindingResult bindingResult,
                             @ModelAttribute UpdatePasswordDto updatePasswordDto, @RequestParam String changePwOrNot,
                             Principal principal, Model model){
        Member signInMember = memberService.findMember(principal.getName());
        if(changePwOrNot.equals("Y")){
            String currentPassword = updatePasswordDto.getCurrentPassword();
            String newPassword = updatePasswordDto.getNewPassword();
            if (memberService.isNullPassword(currentPassword)) {
                model.addAttribute("wrongPassword", "현재 비밀번호를 입력하지 않았습니다.");
                return "member/myPage";
            }
            if (memberService.isNullPassword(newPassword)) {
                model.addAttribute("emptyNewPassword", "새 비밀번호를 입력하지 않았습니다.");
                return "member/myPage";
            }
            if(! memberService.updatePassword(updatePasswordDto, signInMember)){
                model.addAttribute("wrongPassword", "입력하신 현재 비밀번호가 틀렸습니다.");
                return "member/myPage";
            }
        }
        if(bindingResult.hasErrors()){
            return "member/myPage";
        }
        memberService.modifyMember(updateMemberDto);
        model.addAttribute("success", "성공적으로 수정되었습니다.");
        return "member/myPage";
    }
}
