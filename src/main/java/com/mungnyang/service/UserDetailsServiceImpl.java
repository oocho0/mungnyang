package com.mungnyang.service;

import com.mungnyang.entity.member.Member;
import com.mungnyang.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberService.findMember(email);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder().username(member.getEmail()).password(member.getPassword()).roles(member.getRole().toString()).build();
    }
}
