package com.mungnyang.service.product.accommodation;

import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.repository.product.accommodation.AccommodationImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationImageService {

    private final AccommodationImageRepository accommodationImageRepository;
    private final ImageService imageService;

    /**
     * AccommodationImage 저장
     *
     * @param accommodation 저장할 AccommodationImage의 Accommodation
     * @param accommodationImageFileList 저장할 AccommodationImage들
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

    public List<AccommodationImage> findAccommodationImageByAccommodation(Accommodation accommodation) {
        List<AccommodationImage> accommodationImages = accommodationImageRepository.findByAccommodationAccommodationIdOrderByAccommodationImageId(accommodation.getAccommodationId());
        if (accommodationImages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return accommodationImages;
    }
}
