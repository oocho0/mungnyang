package com.mungnyang.service.product.store;

import com.mungnyang.entity.product.store.Store;
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
    public void deleteAllStoreComments(Store store) {
        List<StoreComment> savedStoreComments = getStoreCommentListByStore(store);
        for (StoreComment savedStoreComment : savedStoreComments) {
            storeCommentRepository.delete(savedStoreComment);
        }
    }

    private List<StoreComment> getStoreCommentListByStore(Store store) {
        List<StoreComment> findStoreComments = storeCommentRepository.findByStoreStoreIdOrderByStoreCommentId(store.getStoreId());
        if (findStoreComments == null) {
            throw new IllegalArgumentException();
        }
        return findStoreComments;
    }
}
