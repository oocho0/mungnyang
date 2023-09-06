package com.mungnyang.service.product.accommodation;

import com.mungnyang.dto.product.accommodation.ModifyFacilityDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import com.mungnyang.repository.product.accommodation.AccommodationFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationFacilityService {
    private final AccommodationFacilityRepository accommodationFacilityRepository;

    /**
     * 신규 숙소 시설 저장
     *
     * @param accommodation             등록할 숙소 정보의 숙소 객체
     * @param accommodationFacilityList 페이지에 입력한 등록할 숙소 시설 정보 리스트
     */
    public void saveAccommodationFacilities(Accommodation accommodation, List<String> accommodationFacilityList) {
        for (String facilityName : accommodationFacilityList) {
            AccommodationFacility createdAccommodationFacility = new AccommodationFacility();
            createdAccommodationFacility.setFacilityName(facilityName);
            createdAccommodationFacility.setAccommodation(accommodation);
            accommodationFacilityRepository.save(createdAccommodationFacility);
        }
    }

    /**
     * AccommodationId로 화면에 표시될 숙소 시설 정보 리스트 찾기
     *
     * @param accommodationId 해당 숙소의 일련번호
     * @return FacilityDto 리스트
     */
    public List<ModifyFacilityDto> getFacilityDtoListByAccommodationId(Long accommodationId) {
        List<AccommodationFacility> accommodationFacilities = getAccommodationFacilityListByAccommodationId(accommodationId);
        List<ModifyFacilityDto> modifyFacilityDtoList = new ArrayList<>();
        for (AccommodationFacility accommodationFacility : accommodationFacilities) {
            modifyFacilityDtoList.add(ModifyFacilityDto.builder()
                    .facilityId(accommodationFacility.getAccommodationFacilityId())
                    .facilityName(accommodationFacility.getFacilityName())
                    .build());
        }
        return modifyFacilityDtoList;
    }

    /**
     * AccommodationId로 숙소 시설 리스트 찾기
     *
     * @param accommodationId 해당 숙소의 일련번호
     * @return 해당 숙소의 AccommodationFacility 엔티티 리스트
     */
    public List<AccommodationFacility> getAccommodationFacilityListByAccommodationId(Long accommodationId) {
        List<AccommodationFacility> accommodationFacilities = accommodationFacilityRepository.findByAccommodationAccommodationIdOrderByAccommodationFacilityId(accommodationId);
        if (accommodationFacilities.isEmpty()) {
            throw new IllegalStateException();
        }
        return accommodationFacilities;
    }

    /**
     * 모든 숙소 시설 삭제 하기
     * @param accommodationId 해당 숙소 일련번호
     */
    public void deleteAccommodationFacilities(Long accommodationId) {
        List<AccommodationFacility> savedFacilities = getAccommodationFacilityListByAccommodationId(accommodationId);
        for (AccommodationFacility savedFacility : savedFacilities) {
            accommodationFacilityRepository.delete(savedFacility);
        }
    }

    /**
     * 숙소 시설 정보 수정하기
     * @param accommodation 해당 숙소 일련번호
     * @param facilityList 수정할 숙소 정보 리스트
     */
    public void updateAccommodationFacilities(Accommodation accommodation, List<ModifyFacilityDto> facilityList) {
        for (ModifyFacilityDto modifyFacilityDto : facilityList) {
            if (modifyFacilityDto.getIsDelete().equals("Y")) {
                Long facilityId = modifyFacilityDto.getFacilityId();
                AccommodationFacility savedFacility = getAccommodationFacilityByAccommodationFacilityId(facilityId);
                accommodationFacilityRepository.delete(savedFacility);
                continue;
            }
            if (modifyFacilityDto.getFacilityId() == null) {
                AccommodationFacility newFacility = new AccommodationFacility();
                newFacility.setFacilityName(modifyFacilityDto.getFacilityName());
                newFacility.setAccommodation(accommodation);
                accommodationFacilityRepository.save(newFacility);
            }
        }
    }

    /**
     * 숙소 시설 일련번호로  숙소 시설 찾기
     * @param accommodationFacilityId 해당 시설 일련번호
     * @return AccommodationFacility 엔티티
     */
    private AccommodationFacility getAccommodationFacilityByAccommodationFacilityId(Long accommodationFacilityId) {
        return accommodationFacilityRepository.findById(accommodationFacilityId).orElseThrow(IllegalArgumentException::new);
    }
}
