package com.mungnyang.service;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.dto.MemberDto;
import com.mungnyang.dto.UpdateMemberDto;
import com.mungnyang.dto.UpdatePasswordDto;
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

    /**
     * 회원 일련번호로 회원 찾기
     * @param memberId 회원 일련 번호
     * @return Member 객체
     */
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 회원 이메일(아이디)로 회원 찾기
     * @param email 회원 아이디
     * @return Member 객체
     */
    public Member findMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            member = memberRepository.findByEmail(Member.ANONYMOUS.getEmail());
        }
        return member;
    }

    /**
     * 회원 아이디 중복 확인 후, 회원 DB에 저장하기
     * @param memberDto 페이지에 입력된 회원 정보
     */
    public void saveMember(MemberDto memberDto) {
        Member member = createMember(memberDto);
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    /**
     * 회원 수정하기
     * @param updateMemberDto 페이지에 입력된 회원 정보
     * @return 회원 일련번호
     */
    public void modifyMember(UpdateMemberDto updateMemberDto) {
        Member member = findMember(updateMemberDto.getMemberId());
        updateMember(updateMemberDto, member);
    }

    /**
     * 회원 아이디 중복 확인, 중복 시 예외 발생
     * @param member 중복 확인할 회원 객체
     */
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }
    }

    /**
     * 회원 가입을 위한 MemberDto -> Member
     * 회원 비밀번호와 회원 권한은 인코딩 처리
     * 회원 일련번호는 null
     * 회원 주소는 Address 객체로 변환
     * @param memberDto 페이지에서 입력받은 회원 정보
     * @return 생성된 Member 객체
     */
    private Member createMember(MemberDto memberDto) {
        modelMapper.typeMap(MemberDto.class, Member.class).addMappings(mapper -> {
            mapper.using((Converter<String, String>) ctx -> encodedPassword(ctx.getSource())).map(MemberDto::getPassword, Member::setPassword);
            mapper.using((Converter<String, Role>) ctx -> StringUtils.equals(ctx.getSource(), "admin") ? Role.ADMIN : Role.USER).map(MemberDto::getRole, Member::setRole);
            mapper.skip(Member::setAddress);
            mapper.skip(Member::setMemberId);
        });
        Member createdMember = modelMapper.map(memberDto, Member.class);
        createdMember.setAddress(Address.builder()
                            .zipcode(memberDto.getZipcode())
                            .address(memberDto.getAddress())
                            .detail(memberDto.getAddressDetail())
                            .addition(memberDto.getAddressExtra())
                        .build());
        return createdMember;
    }

    /**
     * 회원 정보 수정(이름, 전화번호, 주소만 가능)
     * @param updateMemberDto 페이지에서 입력받은 회원 정보
     * @param member 수정될 Member 객체
     * @return 수정된 member
     */
    private Member updateMember(UpdateMemberDto updateMemberDto, Member member) {
        member.setName(updateMemberDto.getName());
        member.setTel(updateMemberDto.getTel());
        member.setAddress(Address.builder()
                        .zipcode(updateMemberDto.getZipcode())
                        .address(updateMemberDto.getAddress())
                        .detail(updateMemberDto.getAddressDetail())
                        .addition(updateMemberDto.getAddressExtra())
                .build());
        return member;
    }

    /**
     * 비밀번호 Null 체크
     * @param password
     * @return Null이거나 ""이면 true 반환
     */
    public boolean isNullPassword(String password) {
        if (password == null || password.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 현재 비밀번호 확인 후, 비밀번호 수정
     * 현재 비밀번호가 틀릴시 false 반환
     * @param updatePasswordDto 페이지에 입력된 비밀번호들
     * @param signInMember 로그인된 회원 member
     * @return 현재 비밀번호 확인 여부
     */
    public boolean updatePassword(UpdatePasswordDto updatePasswordDto, Member signInMember){
        String inputPassword = updatePasswordDto.getCurrentPassword();
        String currentPassword = signInMember.getPassword();
        String newPassword = updatePasswordDto.getNewPassword();

        if(encodedPassword(inputPassword).equals(currentPassword)){
            signInMember.setPassword(encodedPassword(newPassword));
            return true;
        }
        return false;
    }

    /**
     * 비밀번호 인코딩
     * @param password 입력된 비밀번호
     * @return 인코딩된 비밀번호
     */
    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Member -> UpdateMemberDto
     * @param member
     * @return 페이지에 표시될 회원 정보
     */
    public UpdateMemberDto of(Member member) {
        modelMapper.typeMap(Member.class, UpdateMemberDto.class).addMappings(mapping -> {
            mapping.map(source -> source.getAddress().getZipcode(), UpdateMemberDto::setZipcode);
            mapping.map(source -> source.getAddress().getAddress(), UpdateMemberDto::setAddress);
            mapping.map(source -> source.getAddress().getDetail(), UpdateMemberDto::setAddressDetail);
            mapping.map(source -> source.getAddress().getAddition(), UpdateMemberDto::setAddressExtra);
            mapping.using((Converter<String, String>) ctx -> StringUtils.equals(ctx.getSource(), "kakao") ?
                            MemberType.KAKAO : MemberType.NORMAL)
                    .map(Member::getMemberType, UpdateMemberDto::setMemberType);
        });
        return modelMapper.map(member, UpdateMemberDto.class);
    }

    /**
     * MemberService 빈 생성 시 기본 회원을 DB에 저장
     */
    @PostConstruct
    public void init() {
        if (memberRepository.findByEmail(Member.ANONYMOUS.getEmail()) == null) {
            memberRepository.save(Member.ANONYMOUS);
        }
        if (memberRepository.findByEmail("admin@abc.com") == null) {
            MemberDto admin = new MemberDto();
            admin.setRole("admin");
            admin.setName("관리자");
            admin.setEmail("admin@abc.com");
            admin.setPassword("12345678");
            admin.setMemberType(MemberType.KAKAO);
            saveMember(admin);
        }
        if (memberRepository.findByEmail("user@abc.com") == null) {
            MemberDto user = new MemberDto();
            user.setRole("user");
            user.setName("사용자");
            user.setEmail("user@abc.com");
            user.setPassword("12345678");
            user.setMemberType(MemberType.NORMAL);
            saveMember(user);
        }
    }
}
