package com.mungnyang.controller;

import com.mungnyang.constant.Role;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.service.MemberService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/new")
    public String signUpMain(){
        return "member/signUpMain";
    }

    @GetMapping("/new/{role}")
    public String signUpAdmin(@PathVariable String role, Model model) {
        MemberDto memberDto = new MemberDto();
        if(role.equals("admin")){
            memberDto.setRole("admin");
        }else{
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
    public String login(){
        return "member/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "member/login";
    }
}
