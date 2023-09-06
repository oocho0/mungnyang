package com.mungnyang.service.product.accommodation;

import com.mungnyang.dto.product.accommodation.FacilityDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import com.mungnyang.repository.product.accommodation.AccommodationFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationFacilityService {
    private final AccommodationFacilityRepository accommodationFacilityRepository;
    private final ModelMapper modelMapper;

    /**
     * 신규 숙소 시설 저장
     * @param accommodation 등록할 숙소 정보의 숙소 객체
     * @param accommodationFacilityList 페이지에 입력한 등록할 숙소 시설 정보 리스트
     */
    public void saveAccommodationFacilities(Accommodation accommodation, List<String> accommodationFacilityList){
        for (String facilityName : accommodationFacilityList) {
            AccommodationFacility createdAccommodationFacility = new AccommodationFacility();
            createdAccommodationFacility.setFacilityName(facilityName);
            createdAccommodationFacility.setAccommodation(accommodation);
            accommodationFacilityRepository.save(createdAccommodationFacility);
        }
    }

    /**
     * AccommodationId로 화면에 표시될 숙소 시설 정보 리스트 찾기
     * @param accommodationId 해당 숙소의 일련번호
     * @return FacilityDto 리스트
     */
    public List<FacilityDto> getFacilityDtoListByAccommodationId(Long accommodationId) {
        List<AccommodationFacility> accommodationFacilities = getAccommodationFacilityListByAccommodationId(accommodationId);
        List<FacilityDto> facilityDtoList = new ArrayList<>();
        for (AccommodationFacility accommodationFacility : accommodationFacilities) {
            facilityDtoList.add(FacilityDto.builder()
                    .facilityId(accommodationFacility.getAccommodationFacilityId())
                    .facilityName(accommodationFacility.getFacilityName())
                    .includedId(accommodationFacility.getAccommodation().getAccommodationId())
                    .build());
        }
        return facilityDtoList;
    }

    /**
     * AccommodationId로 숙소 시설 리스트 찾기
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

}
