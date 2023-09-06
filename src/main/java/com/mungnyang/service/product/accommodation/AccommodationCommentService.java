package com.mungnyang.service.product.accommodation;

import com.mungnyang.entity.product.accommodation.AccommodationComment;
import com.mungnyang.repository.product.accommodation.AccommodationCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationCommentService {
    private final AccommodationCommentRepository accommodationCommentRepository;

    /**
     * 해당 숙소의 모든 후기 삭제
     * @param accommodationId 해당 숙소의 일련번호
     */
    public void deleteAllAccommodationComments(Long accommodationId) {
        List<AccommodationComment> savedAccommodationComments = getAccommodationCommentListByAccommodationId(accommodationId);
        for (AccommodationComment savedAccommodationComment : savedAccommodationComments) {
            accommodationCommentRepository.delete(savedAccommodationComment);
        }
    }

    /**
     * accommodationId로 후기 리스트 찾기
     * @param accommodationId 해당 숙소 일련번호
     * @return 해당 숙소의 AccommodationComment 엔티티 리스트
     */
    private List<AccommodationComment> getAccommodationCommentListByAccommodationId(Long accommodationId) {
        List<AccommodationComment> findAccommodationCommentList = accommodationCommentRepository.findByAccommodationAccommodationIdOrderByAccommodationCommentId(accommodationId);
        if (findAccommodationCommentList == null) {
            throw new IllegalArgumentException();
        }
        return findAccommodationCommentList;
    }
}
