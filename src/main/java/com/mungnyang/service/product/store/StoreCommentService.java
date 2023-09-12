package com.mungnyang.service.product.store;

import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.dto.product.CreateCommentDto;
import com.mungnyang.entity.product.Comment;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreComment;
import com.mungnyang.repository.product.store.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreCommentService {
    private final StoreCommentRepository storeCommentRepository;

    /**
     * 편의 시설 신규 후기 등록
     *
     * @param store            헤딩 편의 시설
     * @param createCommentDto 페이지에 등록된 후기 정보
     */
    public void saveStoreComment(Store store, CreateCommentDto createCommentDto) {
        storeCommentRepository.save(StoreComment.builder()
                .comment(Comment.builder()
                        .commentContent(createCommentDto.getContent())
                        .rate(createCommentDto.getRate())
                        .build())
                .store(store)
                .build());
    }

    /**
     * 한 개의 편의 시설 후기 삭제
     * @param storeCommentId 해당 편의 시설 후기 일련번호
     */
    public void deleteStoreComment(Long storeCommentId) {
        StoreComment savedStoreComment = getStoreCommentByStoreCommentId(storeCommentId);
        storeCommentRepository.delete(savedStoreComment);
    }
    /**
     * 해당 편의 시설의 모든 후기 삭제
     *
     * @param storeId 해당 편의 시설 일련번호
     */
    public void deleteAllStoreComments(Long storeId) {
        List<StoreComment> savedStoreComments = getStoreCommentListByStoreId(storeId);
        for (StoreComment savedStoreComment : savedStoreComments) {
            storeCommentRepository.delete(savedStoreComment);
        }
    }

    /**
     * 편의 시설 디테일 페이지 처음 렌더링 시 사용하는 편의 시설 후기 페이지 반환 메서드
     *
     * @param storeId 해당 편의 시설 일련번호
     * @return 페이징 조건의 더해진 CommentDto 리스트
     */
    public Page<CommentDto> getCommentPage(Long storeId) {
        Pageable pageable = PageRequest.of(0, 5);
        return getCommentPage(storeId, pageable);
    }

    /**
     * 편의 시설의 후기 페이징 반환
     *
     * @param storeId  해당 편의 시설 일련번호
     * @param pageable 페이지 조건
     * @return 페이징 조건이 더해진 CommentDto 리스트
     */
    public Page<CommentDto> getCommentPage(Long storeId, Pageable pageable) {
        return storeCommentRepository.getStoreCommentDtoPaging(storeId, pageable);
    }

    /**
     * 편의 시설 후기 평점 평균 반환
     *
     * @param storeId 해당 편의 시설 일련번호
     * @return 평점 평균
     */
    public Float getRateAverage(Long storeId) {
        Float result = storeCommentRepository.getRateAverage(storeId);
        if (result == null) {
            return 0.0F;
        }
        return result;
    }

    /**
     * 편의 시설 후기 개수 반환
     *
     * @param storeId 해당 편의 시설 일련번호
     * @return 후기 개수
     */
    public Long getCommentCount(Long storeId) {
        return storeCommentRepository.countByStoreStoreId(storeId);
    }

    /**
     * StoreId로 후기 리스트 찾기
     *
     * @param storeId 해당 편의 시설 일련번호
     * @return 해당 편의 시설의 StoreComment 엔티티 리스트
     */
    public List<StoreComment> getStoreCommentListByStoreId(Long storeId) {
        List<StoreComment> findStoreComments = storeCommentRepository.findByStoreStoreIdOrderByStoreCommentId(storeId);
        if (findStoreComments == null) {
            throw new IllegalArgumentException();
        }
        return findStoreComments;
    }

    /**
     * 현재 접속 회원의 아이디와 해당 편의 시설 후기의 작성자가 동일하지 않으면 문제가 있음
     * @param storeCommentId 해당 편의 시설 후기의 일련번호
     * @param email 현재 접속 회원의 아이디이자 이메일
     * @return 동일하지 않으면 true, 동일하면 문제 없으므로 false
     */
    public boolean isNotWrittenByPrinciple(Long storeCommentId, String email) {
        StoreComment storeComment = getStoreCommentByStoreCommentId(storeCommentId);
        return !email.equals(storeComment.getCreatedBy());
    }

    /**
     * Url로 넘어온 storeId와 StoreCommentId의 storeId가 동일하지 판단해서 다르면 문제
     * @param storeId Url로 넘어온 storeId
     * @param storeCommentId Url로 넘어온 StoreCommentId
     * @return 동일하지 않으면 true, 동일하면 문제없으므로 false
     */
    public boolean isNotOneOfStoreComment(Long storeId, Long storeCommentId) {
        StoreComment storeComment = getStoreCommentByStoreCommentId(storeCommentId);
        return !Objects.equals(storeId, storeComment.getStore().getStoreId());
    }

    /**
     * StoreCommentId로 StoreComment 찾기
     * @param storeCommentId 해당 편의 시설 후기 일련번호
     * @return 찾은 편의 시설 StoreComment 엔티티
     */
    public StoreComment getStoreCommentByStoreCommentId(Long storeCommentId) {
        return storeCommentRepository.findById(storeCommentId).orElseThrow(IllegalArgumentException::new);
    }
}
