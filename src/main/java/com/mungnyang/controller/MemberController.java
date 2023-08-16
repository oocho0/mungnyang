package com.mungnyang.controller;

import com.mungnyang.dto.MemberDto;
import com.mungnyang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/new")
    public String signUp(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/signIn";
    }

    @PostMapping("/new")
    public String signup(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/member/signIn";
        }
        try {
            memberService.saveMember(memberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/signIn";
        }
        return "redirect:/";
    }
}
