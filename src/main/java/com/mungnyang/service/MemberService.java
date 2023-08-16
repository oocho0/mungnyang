package com.mungnyang.service;

import com.mungnyang.dto.MemberDto;
import com.mungnyang.entity.Member;
import com.mungnyang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    public Member findMember(String email){
        Member member = memberRepository.findByEmail(email);
        if(member == null) {
            throw new IllegalArgumentException("가입된 회원이 아닙니다.");
        }
        return member;
    }

    public void saveMember(MemberDto memberDto){
        Member member = createMember(memberDto);
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void encodingPw(MemberDto memberDto) {
        String enteredPassword = memberDto.getPassword();
        String encodedPassword = passwordEncoder.encode(enteredPassword);
        memberDto.setEncodedPassword(encodedPassword);
    }
    public Member createMember(MemberDto memberDto) {
        encodingPw(memberDto);
        modelMapper.typeMap(MemberDto.class, Member.class).addMappings(mapper -> {
            mapper.map(MemberDto::getEncodedPassword, Member::setPassword);
        });
        return modelMapper.map(memberDto, Member.class);
    }

}
