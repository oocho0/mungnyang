package com.mungnyang.config;

import com.mungnyang.entity.Member;
import com.mungnyang.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Member> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        if(authentication != null){
            member = memberRepository.findByEmail(authentication.getName());
            if (member == null) {
                member = memberRepository.findByEmail(Member.ANONYMOUS.getEmail());
            }
            return Optional.of(member);
        }
        return Optional.empty();
    }
}
