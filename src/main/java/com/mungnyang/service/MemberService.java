package com.mungnyang.service;

import com.mungnyang.constant.Role;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.entity.Address;
import com.mungnyang.entity.Member;
import com.mungnyang.repository.MemberRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    public Member findMember(String email){
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            member = Member.ANONYMOUS;
        }
        return member;
    }

    public void saveMember(MemberDto memberDto){
        Member member = createMember(memberDto);
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    public Boolean isSavedMember(String kakaoEmail){
        Member member = findMember(kakaoEmail);
        if(member.getName().equals(Member.ANONYMOUS.getName())){
            return false;
        }
        return true;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }
    }


    public Member createMember(MemberDto memberDto) {
        modelMapper.typeMap(MemberDto.class, Member.class).addMappings(mapper -> {
            mapper.map(MemberDto::getEncodedPassword, Member::setPassword);
            mapper.using((Converter<String, String>) ctx -> passwordEncoder.encode(ctx.getSource())).map(MemberDto::getPassword, Member::setPassword);
            mapper.using((Converter<String, Role>) ctx -> StringUtils.equals(ctx.getSource(), "admin") ? Role.ADMIN : Role.USER).map(MemberDto::getRole, Member::setRole);
            mapper.skip(Member::setAddress);
            mapper.skip(Member::setMemberId);
        });
        Member member =  modelMapper.map(memberDto, Member.class);
        member.setAddress(
                Address.builder()
                        .zipcode(memberDto.getZipcode())
                        .address(memberDto.getAddress())
                        .detail(memberDto.getAddressDetail())
                        .addition(memberDto.getAddressExtra())
                        .build()
        );
        return member;
    }

    @PostConstruct
    public void init(){
        if(memberRepository.findByEmail(Member.ANONYMOUS.getEmail()) == null){
            memberRepository.save(Member.ANONYMOUS);
        }
        if(memberRepository.findByEmail("admin@abc.com") == null){
            MemberDto admin = new MemberDto();
            admin.setRole("admin");
            admin.setName("관리자");
            admin.setEmail("admin@abc.com");
            admin.setPassword("12345678");
            saveMember(admin);
        }
        if(memberRepository.findByEmail("user@abc.com") == null){
            MemberDto user = new MemberDto();
            user.setRole("user");
            user.setName("사용자");
            user.setEmail("user@abc.com");
            user.setPassword("12345678");
            saveMember(user);
        }
    }
}
