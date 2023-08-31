package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.accommodation.AccommodationFacilityDto;
import com.mungnyang.entity.product.Facility;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import com.mungnyang.repository.product.accommodation.AccommodationFacilityRepository;
import com.mungnyang.service.product.StatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveAccommodationFacilities(Accommodation accommodation, List<AccommodationFacilityDto> accommodationFacilityList){
        for (AccommodationFacilityDto accommodationFacilityDto : accommodationFacilityList) {
            modelMapper.typeMap(AccommodationFacilityDto.class, Facility.class).addMappings(mapping -> {
                mapping.using((Converter<String, IsTrue>) ctx -> StatusService.isTrueConverter(ctx.getSource())).map(AccommodationFacilityDto::getFacilityIsExist, Facility::setIsExist);
                mapping.map(AccommodationFacilityDto::getFacilityName, Facility::setName);
            });
            Facility createdFacility = modelMapper.map(accommodationFacilityDto, Facility.class);
            AccommodationFacility createdAccommodationFacility = new AccommodationFacility();
            createdAccommodationFacility.setFacility(createdFacility);
            createdAccommodationFacility.setAccommodation(accommodation);
            accommodationFacilityRepository.save(createdAccommodationFacility);
        }
    }

    public List<AccommodationFacility> findAccommodationFacilityByAccommodation(Accommodation accommodation) {
        List<AccommodationFacility> accommodationFacilities = accommodationFacilityRepository.findByAccommodationAccommodationIdOrderByAccommodationFacilityId(accommodation.getAccommodationId());
        if (accommodationFacilities.isEmpty()) {
            throw new IllegalStateException();
        }
        return accommodationFacilities;
    }
}
