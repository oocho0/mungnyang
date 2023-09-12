package com.mungnyang.service.product.accommodation;

import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.dto.product.CreateCommentDto;
import com.mungnyang.entity.product.Comment;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationComment;
import com.mungnyang.repository.product.accommodation.AccommodationCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationCommentService {
    private final AccommodationCommentRepository accommodationCommentRepository;

    /**
     * 숙소 신규 후기 등록
     *
     * @param accommodation 해당 후기
     * @param createCommentDto   페이지에 등록된 후기 정보
     */
    public void saveAccommodationComment(Accommodation accommodation, CreateCommentDto createCommentDto) {
        accommodationCommentRepository.save(AccommodationComment.builder()
                .comment(Comment.builder()
                        .rate(createCommentDto.getRate())
                        .commentContent(createCommentDto.getContent())
                        .build())
                .accommodation(accommodation)
                .build());
    }

    /**
     * 한 개의 숙소 후기 삭제
     * @param accommodationCommentId 헤딩 숙소 후기 일련번호
     */
    public void deleteAccommodationComment(Long accommodationCommentId) {
        AccommodationComment savedAccommodationComment = getAccommodationCommentByAccommodationCommentId(accommodationCommentId);
        accommodationCommentRepository.delete(savedAccommodationComment);
    }

    /**
     * 해당 숙소의 모든 후기 삭제
     *
     * @param accommodationId 해당 숙소의 일련번호
     */
    public void deleteAllAccommodationComments(Long accommodationId) {
        List<AccommodationComment> savedAccommodationComments = getAccommodationCommentListByAccommodationId(accommodationId);
        for (AccommodationComment savedAccommodationComment : savedAccommodationComments) {
            accommodationCommentRepository.delete(savedAccommodationComment);
        }
    }

    /**
     * 숙소 디테일 페이지 처음 렌더링 시 사용하는 숙소 후기 페이지 반환 메서드
     *
     * @param accommodationId 해당 숙소 일련번호
     * @return 페이징 조건이 더해진 CommentDto 리스트
     */
    public Page<CommentDto> getCommentPage(Long accommodationId) {
        Pageable pageable = PageRequest.of(0, 5);
        return getCommentPage(accommodationId, pageable);
    }

    /**
     * 숙소의 후기 페이징 반환
     *
     * @param accommodationId 해당 숙소 일련번호
     * @param pageable        페이지 조건
     * @return 페이징 조건이 더해진 CommentDto 리스트
     */
    public Page<CommentDto> getCommentPage(Long accommodationId, Pageable pageable) {
        return accommodationCommentRepository.getAccommodationCommentDtoPaging(accommodationId, pageable);
    }

    /**
     * 숙소 후기 평점 평균 반환
     *
     * @param accommodationId 해당 숙소 일련번호
     * @return 평점 평균
     */
    public Float getRateAverage(Long accommodationId) {
        Float result = accommodationCommentRepository.getRateAverage(accommodationId);
        if (result == null) {
            return 0.0F;
        }
        return result;
    }

    /**
     * 숙소 후기 개수 반환
     *
     * @param accommodationId 해당 숙소 일련번호
     * @return 후기 개수
     */
    public Long getCommentCount(Long accommodationId) {
        return accommodationCommentRepository.countByAccommodationAccommodationId(accommodationId);
    }

    /**
     * accommodationId로 후기 리스트 찾기
     *
     * @param accommodationId 해당 숙소 일련번호
     * @return 해당 숙소의 AccommodationComment 엔티티 리스트
     */
    public List<AccommodationComment> getAccommodationCommentListByAccommodationId(Long accommodationId) {
        List<AccommodationComment> findAccommodationCommentList = accommodationCommentRepository.findByAccommodationAccommodationIdOrderByAccommodationCommentId(accommodationId);
        if (findAccommodationCommentList == null) {
            throw new IllegalArgumentException();
        }
        return findAccommodationCommentList;
    }

    /**
     * 현재 접속 회원의 아이디와 해당 숙소 후기의 작성자가 동일하지 않으면 문제가 있음
     * @param accommodationCommnetId 해당 숙소 후기의 일련번호
     * @param email 현재 접속 회원의 아이디이자 이메일
     * @return 동일하지 않으면 true, 동일하면 문제 없으므로 false
     */
    public boolean isNotWrittenByPrinciple(Long accommodationCommnetId, String email) {
        AccommodationComment accommodationComment = getAccommodationCommentByAccommodationCommentId(accommodationCommnetId);
        return !email.equals(accommodationComment.getCreatedBy());
    }

    /**
     *  Url로 넘어온 accommodationId와 accommodationCommentId의 accommodationId가 동일하지 판단해서 다르면 문제
     * @param accommodationId Url로 넘어온 accommodationId
     * @param accommodationCommentId Url로 넘어온 accommodationCommentId
     * @return 동일하지 않으면 true, 동일하면 문제없으므로 false
     */
    public boolean isNotOneOfAccommodationComment(Long accommodationId, Long accommodationCommentId) {
        AccommodationComment accommodationComment = getAccommodationCommentByAccommodationCommentId(accommodationCommentId);
        return !Objects.equals(accommodationId, accommodationComment.getAccommodation().getAccommodationId());
    }

    /**
     * AccommodationCommentId로 AccommodationComment 찾기
     * @param accommodationCommentId 해당 숙소 후기 일련번호
     * @return 찾은 숙소 AccommodationComment 엔티티
     */
    public AccommodationComment getAccommodationCommentByAccommodationCommentId(Long accommodationCommentId) {
        return accommodationCommentRepository.findById(accommodationCommentId).orElseThrow(IllegalArgumentException::new);
    }
}
