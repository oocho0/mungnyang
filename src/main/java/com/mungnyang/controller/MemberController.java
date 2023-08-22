package com.mungnyang.controller;

import com.mungnyang.constant.Kakao;
import com.mungnyang.dto.KakaoAuthResponseDto;
import com.mungnyang.dto.KakaoInfoDto;
import com.mungnyang.dto.KakaoTokenResponseDto;
import com.mungnyang.dto.MemberDto;
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

    @GetMapping("/kakao-auth")
    public String requestAuth(@RequestParam String csrf){
        return "redirect:" + kakaoService.authRequest(csrf);
    }

    @GetMapping("/kakao-token")
    public String reqeustToken(HttpServletRequest request, @ModelAttribute KakaoAuthResponseDto kakaoAuth, Model model){
        String csrf = kakaoAuth.getState();
        String code = kakaoAuth.getCode();
        ResponseEntity<KakaoTokenResponseDto> response = kakaoService.tokenRequest(code);
        KakaoInfoDto kakaoInfoDto = kakaoService.getLoginInfo(response);
        String kakaoEmail = kakaoInfoDto.getSub() + Kakao.EMAIL;
        String kakaoName = kakaoInfoDto.getNickname();
        String kakaoPW = memberService.savedMemberPW(kakaoEmail);
        if(kakaoPW != null){
            kakaoService.loginWithKakao(request, kakaoEmail, kakaoPW);
            return "redirect:/";
        }
        MemberDto memberDto = new MemberDto();
        memberDto.setRole("user");
        memberDto.setName(kakaoName);
        memberDto.setEmail(kakaoEmail);
        memberDto.setPassword(kakaoInfoDto.getSub());
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("isKakao", "Y");
        return "member/signUp";
    }

    @GetMapping("/new/{role}")
    public String signUpAdmin(@PathVariable String role, Model model) {
        MemberDto memberDto = new MemberDto();
        if (role.equals("admin")) {
            memberDto.setRole("admin");
        } else {
            memberDto.setRole("user");
        }
        model.addAttribute("isKakao", "N");
        model.addAttribute("memberDto", memberDto);
        return "member/signUp";
    }

    @PostMapping("/new/{role}")
    public String signUp(HttpServletRequest request, @ModelAttribute @Valid MemberDto memberDto,
                         @RequestParam String isKakao, BindingResult bindingResult, Model model) {
        model.addAttribute("isKakao", isKakao);
        if (bindingResult.hasErrors()) {
            return "member/signUp";
        }
        try {
            memberService.saveMember(memberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signUp";
        }
        if(isKakao.equals("Y")){
            kakaoService.loginWithKakao(request, memberDto.getEmail(), memberDto.getPassword());
            return "redirect:/";
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
