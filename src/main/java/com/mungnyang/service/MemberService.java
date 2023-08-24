package com.mungnyang.service;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.dto.member.MemberDto;
import com.mungnyang.dto.member.UpdateMemberDto;
import com.mungnyang.dto.member.UpdatePasswordDto;
import com.mungnyang.entity.member.MemberAddress;
import com.mungnyang.entity.member.Member;
import com.mungnyang.repository.MemberRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
     *
     * @param memberId 회원 일련 번호
     * @return Member 객체
     */
    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 회원 이메일(아이디)로 회원 찾기
     *
     * @param email 회원 아이디
     * @return Member 객체
     */
    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email);
    }

    /**
     * 회원 아이디 중복 확인 후, 회원 DB에 저장하기
     *
     * @param memberDto 페이지에 입력된 회원 정보
     */
    public void saveMember(MemberDto memberDto) {
        Member member = createMember(memberDto);
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    /**
     * 회원 아이디 중복 확인, 중복 시 예외 발생
     *
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
     *
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
        createdMember.setAddress(MemberAddress.builder()
                .zipcode(memberDto.getZipcode())
                .address(memberDto.getAddress())
                .detail(memberDto.getAddressDetail())
                .addition(memberDto.getAddressExtra())
                .build());
        return createdMember;
    }

    /**
     * 회원 정보 수정(이름, 전화번호, 주소만 가능)
     *
     * @param updateMemberDto 페이지에서 입력받은 회원 정보
     * @param member          수정될 Member 객체
     * @return 수정된 member
     */
    public void updateMember(UpdateMemberDto updateMemberDto, Member member) {
        member.setName(updateMemberDto.getName());
        member.setAddress(MemberAddress.builder()
                .zipcode(updateMemberDto.getZipcode())
                .address(updateMemberDto.getAddress())
                .detail(updateMemberDto.getAddressDetail())
                .addition(updateMemberDto.getAddressExtra())
                .build());
        member.setTel(updateMemberDto.getTel());
        memberRepository.save(member);
    }

    /**
     * 비밀번호 Null 체크
     *
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
     *
     * @param updatePasswordDto 페이지에 입력된 비밀번호들
     * @param signInMember      로그인된 회원 member
     * @return 현재 비밀번호 확인 여부
     */
    public List<String> updatePassword(UpdatePasswordDto updatePasswordDto, Member signInMember) {
        String inputPassword = updatePasswordDto.getCurrentPassword();
        String newPassword = updatePasswordDto.getNewPassword();
        String rightPassword = signInMember.getPassword();
        List<String> result = new ArrayList<>();
        if (isNullPassword(inputPassword)) {
            result.add("wrongPassword");
            result.add("현재 비밀번호를 입력하지 않았습니다.");
            return result;
        }
        if (!(newPassword.length() > 7 &&newPassword.length() < 17)) {
            result.add("emptyNewPassword");
            result.add("비밀번호 형식에 맞게 입력해주세요.");
            return result;
        }
        if (isNullPassword(newPassword)) {
            result.add("emptyNewPassword");
            result.add("새 비밀번호를 입력하지 않았습니다.");
            return result;
        }
        if (inputPassword.equals(newPassword)) {
            result.add("emptyNewPassword");
            result.add("현재 비밀번호와 새 비밀번호가 일치합니다.");
            return result;
        }
        if (!passwordEncoder.matches(inputPassword, rightPassword)) {
            result.add("wrongPassword");
            result.add("입력된 현재 비밀번호가 틀렸습니다.");
            return result;
        }
        signInMember.setPassword(encodedPassword(newPassword));
        memberRepository.save(signInMember);
        return null;
    }

    /**
     * 비밀번호 인코딩
     *
     * @param password 입력된 비밀번호
     * @return 인코딩된 비밀번호
     */
    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Member -> UpdateMemberDto
     *
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
        if (memberRepository.findByEmail("admin@abc.com") == null) {
            MemberDto admin = new MemberDto();
            admin.setName("관리자");
            admin.setEmail("admin@abc.com");
            admin.setPassword("12345678");
            admin.setZipcode("00000");
            admin.setAddress("한국");
            admin.setAddressDetail("서울");
            admin.setAddressExtra("어딘가");
            admin.setTel("010-1234-1234");
            admin.setRole("admin");
            admin.setMemberType(MemberType.NORMAL);
            saveMember(admin);
        }
        if (memberRepository.findByEmail("user@abc.com") == null) {
            MemberDto user = new MemberDto();
            user.setName("사용자");
            user.setEmail("user@abc.com");
            user.setPassword("12345678");
            user.setZipcode("00000");
            user.setAddress("한국");
            user.setAddressDetail("서울");
            user.setAddressExtra("어딘가");
            user.setTel("010-4321-4321");
            user.setRole("user");
            user.setMemberType(MemberType.NORMAL);
            saveMember(user);
        }
    }
}
