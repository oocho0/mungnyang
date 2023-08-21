package com.mungnyang.controller;

import com.mungnyang.constant.Kakao;
import com.mungnyang.dto.KakaoAuthResponseDto;
import com.mungnyang.dto.KakaoInfoDto;
import com.mungnyang.dto.KakaoTokenResponseDto;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.service.KakaoService;
import com.mungnyang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    @GetMapping("/new")
    public String signUpMain() {
        return "member/signUpMain";
    }

    @GetMapping("/kakao-auth")
    public ResponseEntity<?> requestAuth(@RequestHeader("_csrf") String csrf){
        ResponseEntity<KakaoAuthResponseDto> response = kakaoService.authRequest(csrf);
        if(!response.getStatusCode().equals(HttpStatus.OK) || response.getBody().getError() != null || response.getBody().getError_description() != null){
            return new ResponseEntity<String>(response.getBody().getError_description(), HttpStatus.BAD_REQUEST);
        }
        if(csrf == null || !response.getBody().getState().equals(csrf)){
            return new ResponseEntity<String>("보안 문제로 로그인이 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<KakaoAuthResponseDto>(response.getBody(), HttpStatus.OK);
    }

    @GetMapping("/kakao-token")
    public void reqeustToken(@ModelAttribute KakaoAuthResponseDto kakaoAuth){
        String csrf = kakaoAuth.getState();
        String code = kakaoAuth.getCode();
        ResponseEntity<KakaoTokenResponseDto> response = kakaoService.tokenRequest(code);
        KakaoInfoDto kakaoInfoDto = kakaoService.getLoginInfo(response);
        String kakaoEmail = kakaoInfoDto.getSub() + Kakao.EMAIL;
        if(memberService.isSavedMember(kakaoEmail)){
            kakaoService.loginWithKakao(csrf, kakaoEmail);
            return;
        }


    }

    @GetMapping("/new/{role}")
    public String signUpAdmin(@PathVariable String role, Model model) {
        MemberDto memberDto = new MemberDto();
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
        return "redirect:/member/login";
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
}
