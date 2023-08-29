package com.mungnyang.service.product.accommodation;

import com.mungnyang.dto.product.accommodation.CreateAccommodationDto;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.repository.product.accommodation.AccommodationRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final StateCityService stateCityService;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationImageService accommodationImageService;

    /**
     * 숙소 등록 시 필요한 소분류를 모델에 담아 전달
     * @param model 전달할 모델
     */
    public void initializeStore(Model model) {
        List<SmallCategory> smallCategoryList = categoryService.getSmallCategoriesByBigCategoryId(1L);
        model.addAttribute("smallCategories", smallCategoryList);
    }

    /**
     * 신규 숙소 등록하기
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @param accommodationImageFileList 페이지에 입력한 숙소 이미지들
     * @throws Exception
     */
    public void registerAccommodation(CreateAccommodationDto createAccommodationDto, List<MultipartFile> accommodationImageFileList) throws Exception {
        Accommodation accommodation = createAccommodation(createAccommodationDto);
        accommodationRepository.save(accommodation);
        accommodationImageService.saveAccommodationImages(accommodation, accommodationImageFileList);
    }

    /**
     * 숙소 생성
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @return 생성된 숙소 객체
     */
    private Accommodation createAccommodation(CreateAccommodationDto createAccommodationDto) {
        modelMapper.typeMap(CreateAccommodationDto.class, Accommodation.class).addMappings(mapping -> {
            mapping.skip(Accommodation::setAccommodationId);
            mapping.skip(Accommodation::setCity);
        });
        Accommodation createdAccommodation = modelMapper.map(createAccommodationDto, Accommodation.class);
        createdAccommodation.setCity(stateCityService.getMatchedCity(createAccommodationDto.getProductAddressAddressZipcode()));
        return createdAccommodation;
    }
}
