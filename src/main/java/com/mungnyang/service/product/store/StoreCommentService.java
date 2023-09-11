package com.mungnyang.service.product.store;

import com.mungnyang.entity.product.store.StoreComment;
import com.mungnyang.repository.product.store.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreCommentService {
    private final StoreCommentRepository storeCommentRepository;

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
}
