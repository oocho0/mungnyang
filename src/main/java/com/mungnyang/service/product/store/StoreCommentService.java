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

}