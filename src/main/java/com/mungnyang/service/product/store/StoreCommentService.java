package com.mungnyang.service.product.store;

import com.mungnyang.repository.product.store.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository storeCommentRepository;

    /**
     * 해당 Store에 등록된 후기 개수 반환
     * @param storeId 후기 개수를 알고싶은 Store
     * @return Store의 StoreComment 개수
     */
    public Long getStoreCommentCountByStoreId(Long storeId) {
        return storeCommentRepository.countStoreCommentByStore(storeId);
    }

    /**
     * 해당 Store의 평점 반환
     * @param storeId 평점을 알고 싶은 Store
     * @return Store의 평점
     */
    public Float getStoreRateByStoreId(Long storeId) {
        return storeCommentRepository.findRateByStoreId(storeId);
    }

}
