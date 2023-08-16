package com.mungnyang.controller;

import com.mungnyang.constant.Role;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            memberDto.setRole(Role.ADMIN);
        }else{
            memberDto.setRole(Role.USER);
        }
        model.addAttribute("memberDto", memberDto);
        return "member/signUp";
    }

    @PostMapping("/new")
    public String signUp(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/member/signUp";
        }
        try {
            memberService.saveMember(memberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/signUp";
        }
        return "redirect:/";
    }
}
