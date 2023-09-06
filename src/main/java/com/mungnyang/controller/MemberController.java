package com.mungnyang.controller;

import com.mungnyang.constant.Kakao;
import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.member.kakao.KakaoAuthResponseDto;
import com.mungnyang.dto.member.kakao.KakaoInfoDto;
import com.mungnyang.dto.member.kakao.KakaoTokenResponseDto;
import com.mungnyang.dto.member.CreateMemberDto;
import com.mungnyang.dto.member.KakaoMemberDto;
import com.mungnyang.dto.member.UpdateMemberDto;
import com.mungnyang.dto.member.UpdatePasswordDto;
import com.mungnyang.entity.member.Member;
import com.mungnyang.service.member.KakaoService;
import com.mungnyang.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/new/{role}")
    public String setRole(@PathVariable String role, Model model) {
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setMemberType(MemberType.NORMAL.name());
        if (role.equals(Role.SELLER.name())) {
            createMemberDto.setRole(Role.SELLER.name());
        } else {
            createMemberDto.setRole(Role.USER.name());
        }
        model.addAttribute("createMemberDto", createMemberDto);
        return "member/signUp";
    }

    @PostMapping("/new/{role}")
    public String signUp(@ModelAttribute @Valid CreateMemberDto createMemberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/signUp";
        }
        try {
            memberService.saveMember(createMemberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signUp";
        }
        return "redirect:" + Url.LOGIN;
    }

    @GetMapping("/kakao-auth")
    public String requestAuth(@RequestParam String csrf) {
        return "redirect:" + kakaoService.getKakaoAuthRequestURI(csrf);
    }

    @GetMapping("/kakao-token")
    public String requestToken(HttpServletRequest request, @ModelAttribute KakaoAuthResponseDto kakaoAuth, Model model) {
        String code = kakaoAuth.getCode();
        ResponseEntity<KakaoTokenResponseDto> response = kakaoService.tokenRequest(code);
        KakaoInfoDto kakaoInfoDto = kakaoService.getLoginInfo(response);
        String kakaoEmail = kakaoInfoDto.getSub() + Kakao.EMAIL;
        String kakaoName = kakaoInfoDto.getNickname();
        String kakaoPW = kakaoService.getMemberPW(kakaoEmail);
        if (kakaoPW != null) {
            kakaoService.loginWithKakao(request, kakaoEmail, kakaoPW);
            return "redirect:" + Url.MAIN;
        }
        KakaoMemberDto kakaoMemberDto = new KakaoMemberDto();
        kakaoMemberDto.setName(kakaoName);
        kakaoMemberDto.setEmail(kakaoEmail);
        model.addAttribute("kakaoMemberDto", kakaoMemberDto);
        return "member/kakaoInfo";
    }

    @PostMapping("/new-kakao")
    public String signUpWithKakao(HttpServletRequest request, @ModelAttribute @Valid KakaoMemberDto kakaoMemberDto,
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
    public String logout(Principal principal) {
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        if (signInMember.getMemberType().equals(MemberType.KAKAO)) {
            return "redirect:" + kakaoService.getKakaologoutRequestURI();
        }
        return "redirect:" + Url.LOGOUT;
    }

    @GetMapping("/myPage")
    public String getMemberInfo(@RequestParam(required = false) String success, Principal principal, Model model) {
        if (success != null && success.equals("Y")) {
            model.addAttribute("success", "성공적으로 수정되었습니다.");
        }
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        UpdateMemberDto updateMemberDto = memberService.of(signInMember);
        model.addAttribute("updateMemberDto", updateMemberDto);
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        model.addAttribute("updatePasswordDto", updatePasswordDto);
        return "member/myPage";
    }

    @PostMapping("/modify/{changePwOrNot}")
    public String modifyInfo(@ModelAttribute @Valid UpdateMemberDto updateMemberDto, BindingResult bindingResult,
                             @ModelAttribute UpdatePasswordDto updatePasswordDto, @PathVariable String changePwOrNot,
                             Principal principal, Model model) {
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        if (changePwOrNot.equals("Y")) {
            List<String> result = memberService.updatePassword(updatePasswordDto, signInMember);
            if (result != null) {
                model.addAttribute(result.get(0), result.get(1));
                return "member/myPage";
            }
        }
        if (bindingResult.hasErrors()) {
            return "member/myPage";
        }
        memberService.updateMember(updateMemberDto, signInMember);
        return "redirect:/member/myPage?success=Y";
    }

    @GetMapping("/pre-withdraw")
    public String recheckWithdraw(Principal principal, SessionStatus sessionStatus) {
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        if (signInMember.getMemberType().equals(MemberType.KAKAO)) {
            return "member/withdraw-kakao";
        }
        return "member/withdraw";
    }

    @DeleteMapping("/withdraw-kakao")
    public ResponseEntity<String> withdrawKakaoMember(Principal principal) {
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        String result = kakaoService.withdrawRequest(signInMember.getEmail());
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        memberService.withdrawMember(signInMember);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, String> bodyMap, Principal principal) {
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        List<String> result = memberService.checkPasswordBeforeWithdraw(bodyMap.get("inputPassword"), signInMember);
        if (result != null) {
            return new ResponseEntity<>(result.get(1), HttpStatus.BAD_REQUEST);
        }
        memberService.withdrawMember(signInMember);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
