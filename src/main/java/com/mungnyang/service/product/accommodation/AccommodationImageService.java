package com.mungnyang.service.product.accommodation;

import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.repository.product.accommodation.AccommodationImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationImageService {

    private final AccommodationImageRepository accommodationImageRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    /**
     * AccommodationImage 저장
     * @param accommodation 저장할 AccommodationImage의 Accommodation
     * @param accommodationImageFileList 저장할 AccommodationImage 리스트
     * @throws Exception
     */
    public void saveAccommodationImages(Accommodation accommodation, List<MultipartFile> accommodationImageFileList) throws Exception {
        for (int i = 0; i < accommodationImageFileList.size(); i++) {
            AccommodationImage createdAccommodationImage = new AccommodationImage();
            createdAccommodationImage.setAccommodation(accommodation);
            MultipartFile accommodationImageFile = accommodationImageFileList.get(i);
            createdAccommodationImage.setImage(imageService.createImage(accommodation, accommodationImageFile, i));
            accommodationImageRepository.save(createdAccommodationImage);
        }
    }

    /**
     * AccommodationId 로 숙소 이미지 리스트 찾기
     * @param accommodationId 해당 숙소 일련번호
     * @return 해당 숙소의 AccommodationImage 엔티티 리스트
     */
    public List<AccommodationImage> getAccommodationImageListByAccommodationId(Long accommodationId) {
        List<AccommodationImage> accommodationImages = accommodationImageRepository.findByAccommodationAccommodationIdOrderByAccommodationImageId(accommodationId);
        if (accommodationImages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return accommodationImages;
    }

    /**
     * AccommodationId로 숙소 수정 화면에 나타낼 숙소 이미지 정보 리스트 찾기
     * @param accommodationId 해당 숙소 일련번호
     * @return 해당 숙소의 ModifyImageDto 리스트
     */
    public List<ModifyImageDto> getModifyImageDtoListByAccommodationId(Long accommodationId) {
        List<AccommodationImage> findImages = getAccommodationImageListByAccommodationId(accommodationId);
        List<ModifyImageDto> modifyImageDtoList = new ArrayList<>();
        for (AccommodationImage findImage : findImages) {
            modifyImageDtoList.add(ModifyImageDto.builder()
                    .imageId(findImage.getAccommodationImageId())
                    .imageFileName(findImage.getImage().getFileName())
                    .build());
        }
        return modifyImageDtoList;
    }
}
