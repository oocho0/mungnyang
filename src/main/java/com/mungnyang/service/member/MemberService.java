package com.mungnyang.service.member;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.dto.member.CreateMemberDto;
import com.mungnyang.dto.member.KakaoMemberDto;
import com.mungnyang.dto.member.UpdateMemberDto;
import com.mungnyang.dto.member.UpdatePasswordDto;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.service.Cart;
import com.mungnyang.repository.member.MemberRepository;
import com.mungnyang.service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;


    /**
     * 회원 이메일(아이디)로 회원 찾기
     *
     * @param email 회원 아이디
     * @return Member 객체
     */
    @Transactional(readOnly = true)
    public Member getMemberByMemberEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Cart getCart(String email) {
        Member signInMember = getMemberByMemberEmail(email);
        return cartService.getCartByMemberId(signInMember.getMemberId());
    }


    /**
     * 회원 아이디 중복 확인 후, 회원 DB에 저장하기
     *
     * @param createMemberDto 페이지에 입력된 회원 정보
     */
    public void saveMember(CreateMemberDto createMemberDto) {
        Member member = createMember(createMemberDto);
        validateDuplicateMember(member);
        memberRepository.save(member);
        cartService.saveCart(member);
    }

    /**
     * 카카오 회원 회원 아이디 중복 확인 후, 회원 DB에 저장하기
     *
     * @param kakaoMemberDto 페이지에 입력된 회원 정보
     * @param password       카카오 회원 비밀번호
     */
    public void saveMember(KakaoMemberDto kakaoMemberDto, String password) {
        Member member = createMember(kakaoMemberDto);
        validateDuplicateMember(member);
        member.setMemberType(MemberType.KAKAO);
        member.setRole(Role.USER);
        member.setPassword(encodedPassword(password));
        memberRepository.save(member);
        cartService.saveCart(member);
    }

    /**
     * 회원 아이디 중복 확인, 중복 시 예외 발생
     *
     * @param member 중복 확인할 회원 객체
     */
    private void validateDuplicateMember(Member member) {
        Member findMember = getMemberByMemberEmail(member.getEmail());
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
     * @param createMemberDto 페이지에서 입력받은 회원 정보
     * @return 생성된 Member 객체
     */
    private Member createMember(CreateMemberDto createMemberDto) {
        modelMapper.typeMap(CreateMemberDto.class, Member.class).addMappings(mapping -> {
            mapping.using(
                    (Converter<String, String>) ctx -> encodedPassword(ctx.getSource())
            ).map(CreateMemberDto::getPassword, Member::setPassword);
            mapping.using(
                    (Converter<String, Role>) ctx ->
                            StringUtils.equals(ctx.getSource(), Role.ADMIN.name()) ? Role.ADMIN :
                                    (StringUtils.equals(ctx.getSource(), Role.SELLER.name()) ? Role.SELLER : Role.USER)
            ).map(CreateMemberDto::getRole, Member::setRole);
            mapping.using(
                    (Converter<String, MemberType>) ctx ->
                            StringUtils.equals(ctx.getSource(), MemberType.KAKAO.name()) ? MemberType.KAKAO : MemberType.NORMAL
            ).map(CreateMemberDto::getMemberType, Member::setMemberType);
            mapping.skip(Member::setMemberId);
        });
        return modelMapper.map(createMemberDto, Member.class);
    }

    private Member createMember(KakaoMemberDto kakaoMemberDto) {
        modelMapper.typeMap(KakaoMemberDto.class, Member.class).addMappings(mapping -> {
            mapping.skip(Member::setMemberId);
        });
        return modelMapper.map(kakaoMemberDto, Member.class);
    }

    /**
     * 회원 정보 수정(이름, 전화번호, 주소만 가능)
     *
     * @param updateMemberDto 페이지에서 입력받은 회원 정보
     * @param member          수정될 Member 객체
     * @return 수정된 member
     */
    public void updateMember(UpdateMemberDto updateMemberDto, Member member) {
        modelMapper.typeMap(UpdateMemberDto.class, Member.class).addMappings(mapping -> {
            mapping.skip(Member::setMemberId);
            mapping.skip(Member::setEmail);
            mapping.skip(Member::setMemberType);
        });
        modelMapper.map(updateMemberDto, member);
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
        if (isWrongInputPassword(inputPassword, rightPassword, result)) {
            return result;
        }
        if (!(newPassword.length() > 7 && newPassword.length() < 17)) {
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
        signInMember.setPassword(encodedPassword(newPassword));
        memberRepository.save(signInMember);
        return null;
    }

    /**
     * 계정 탈퇴 전에 비밀번호 확인
     *
     * @param inputPassword 입력한 비밀번호
     * @param signInMember  로그인한 회원 계정 정보
     * @return 올바르지 않으면 result에 결과 반환
     */
    public List<String> checkPasswordBeforeWithdraw(String inputPassword, Member signInMember) {
        String rightPassword = signInMember.getPassword();
        List<String> result = new ArrayList<>();
        if (isWrongInputPassword(inputPassword, rightPassword, result)) {
            return result;
        }
        return null;
    }

    /**
     * 회원 삭제
     *
     * @param member
     */
    public void withdrawMember(Member member) {
        cartService.deleteCart(member);
        memberRepository.delete(member);
    }

    /**
     * 입력한 비밀번호가 올바르지 않는지 확인
     *
     * @param inputPassword 입력한 비밀번호
     * @param rightPassword 회원 계정의 비밀번호
     * @param result        결과를 담을 리스트
     * @return 올바르지 않으면 true
     */

    private boolean isWrongInputPassword(String inputPassword, String rightPassword, List<String> result) {
        if (isNullPassword(inputPassword)) {
            result.add("wrongPassword");
            result.add("현재 비밀번호를 입력하지 않았습니다.");
            return true;
        }
        if (!isRightPassword(inputPassword, rightPassword)) {
            result.add("wrongPassword");
            result.add("입력된 현재 비밀번호가 틀렸습니다.");
            return true;
        }
        return false;
    }

    /**
     * 비밀번호가 옳은지 확인
     *
     * @param inputPassword 입력한 비밀번호
     * @param rightPassword 회원 계정의 비밀번호
     * @return 맞으면 true 틀리면 false
     */
    private boolean isRightPassword(String inputPassword, String rightPassword) {
        return passwordEncoder.matches(inputPassword, rightPassword);
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
            mapping.using(
                    (Converter<MemberType, String>) ctx ->
                            ctx.getSource().equals(MemberType.KAKAO) ?
                                    MemberType.KAKAO.name() : MemberType.NORMAL.name()
            ).map(Member::getMemberType, UpdateMemberDto::setMemberType);
        });
        return modelMapper.map(member, UpdateMemberDto.class);
    }

    /**
     * MemberService 빈 생성 시 기본 회원을 DB에 저장
     */
    @PostConstruct
    private void init() {
        if (getMemberByMemberEmail("admin@abc.com") == null) {
            CreateMemberDto admin = new CreateMemberDto();
            admin.setName("관리자");
            admin.setEmail("admin@abc.com");
            admin.setPassword("12345678");
            admin.setAddressZipcode("00000");
            admin.setAddressMain("한국");
            admin.setAddressDetail("서울");
            admin.setAddressExtra("어딘가");
            admin.setTel("010-1234-1234");
            admin.setRole(Role.ADMIN.name());
            admin.setMemberType(MemberType.NORMAL.name());
            saveMember(admin);
        }
    }
}
