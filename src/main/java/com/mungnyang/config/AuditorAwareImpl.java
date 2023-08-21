package com.mungnyang.config;

import com.mungnyang.entity.Member;
import com.mungnyang.service.MemberService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Member> {

    private MemberService memberService;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        if(authentication != null){
            member = memberService.findMember(authentication.getName());
            return Optional.of(member);
        }
        return Optional.empty();
    }
}
