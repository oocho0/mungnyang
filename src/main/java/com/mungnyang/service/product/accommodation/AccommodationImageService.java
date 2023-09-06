package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.IsTrue;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationImageService {

    private final AccommodationImageRepository accommodationImageRepository;
    private final ImageService imageService;

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

    /**
     * 숙소 이미지 수정
     * @param accommodation 해당 숙소 엔티티
     * @param imageList 수정 페이지에 입력된 수정 이미지 정보
     * @throws Exception
     */
    public void updateAccommodationImages(Accommodation accommodation, List<ModifyImageDto> imageList) throws Exception {
        boolean isRepImageDelete = false;
        for (ModifyImageDto modifyImageDto : imageList) {
            if (modifyImageDto.getIsDelete().equals("Y")) {
                Long imageId = modifyImageDto.getImageId();
                AccommodationImage savedImage = getAccommodationImageByAccommodationImageId(imageId);
                if (savedImage.getImage().getIsRepresentative() == IsTrue.YES) {
                    isRepImageDelete = true;
                }
                imageService.deleteImage(accommodation, savedImage.getImage());
                accommodationImageRepository.delete(savedImage);
                continue;
            }
            if (modifyImageDto.getImageId() == null) {
                AccommodationImage newImage = new AccommodationImage();
                newImage.setAccommodation(accommodation);
                newImage.setImage(imageService.createImage(accommodation, modifyImageDto.getImageFile(), 1));
                accommodationImageRepository.save(newImage);
            }
        }
        if (isRepImageDelete) {
            List<AccommodationImage> currentImages = getAccommodationImageListByAccommodationId(accommodation.getAccommodationId());
            currentImages.get(0).getImage().setIsRepresentative(IsTrue.YES);
        }
    }

    /**
     * 모든 숙소 이미지 삭제
     * @param accommodation 해당 숙소
     * @throws Exception
     */
    public void deleteAllAccommodationImages(Accommodation accommodation) throws Exception {
        List<AccommodationImage> savedAccommodationImages = getAccommodationImageListByAccommodationId(accommodation.getAccommodationId());
        for (AccommodationImage savedAccommodationImage : savedAccommodationImages) {
            imageService.deleteImage(accommodation, savedAccommodationImage.getImage());
            accommodationImageRepository.delete(savedAccommodationImage);
        }
    }

    /**
     * AccommodationImageId로 숙소 이미지 찾기
     * @param accommodationImageId 해당 숙소 이미지 일련번호
     * @return AccommodationImage 엔티티
     */
    private AccommodationImage getAccommodationImageByAccommodationImageId(Long accommodationImageId) {
        return accommodationImageRepository.findById(accommodationImageId).orElseThrow(IllegalArgumentException::new);
    }
}
