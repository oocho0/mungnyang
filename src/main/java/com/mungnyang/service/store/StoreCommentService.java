package com.mungnyang.service.store;

import com.mungnyang.repository.store.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository storeCommentRepository;

}
