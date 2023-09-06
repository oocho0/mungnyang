package com.mungnyang.service.product.store;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreImage;
import com.mungnyang.repository.product.store.StoreImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreImageRepository storeImageRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    /**
     * StoreImage 저장
     * @param store              저장할 StoreImage의 Store
     * @param storeImageFileList 저장할 StoreImage들
     * @throws Exception
     */
    public void saveStoreImages(Store store, List<MultipartFile> storeImageFileList) throws Exception {
        for (int i = 0; i < storeImageFileList.size(); i++) {
            StoreImage createdStoreImage = new StoreImage();
            createdStoreImage.setStore(store);
            MultipartFile storeImageFile = storeImageFileList.get(i);
            createdStoreImage.setImage(imageService.createImage(store, storeImageFile, i));
            storeImageRepository.save(createdStoreImage);
        }
    }

    /**
     * Store로 Store의 이미지 리스트 찾기
     * @param storeId 해당 방의 일련번호
     * @return 해당 Store의 이미지 List
     */
    public List<StoreImage> getStoreImageListByStoreId(Long storeId) {
        List<StoreImage> findImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(storeId);
        if (findImages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return findImages;
    }

    /**
     * 수정 페이지에 출력될 이미지 정보 리스트 찾기
     * @param storeId 찾을 Store의 일련번호
     * @return ModifyImageDto 리스트
     */
    public List<ModifyImageDto> getModifyImageDtoListByStoreId(Long storeId) {
        List<StoreImage> findImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(storeId);
        List<ModifyImageDto> storeImageDtoList = new ArrayList<>();
        for (StoreImage findImage : findImages) {
            storeImageDtoList.add(ModifyImageDto.builder()
                    .imageId(findImage.getStoreImageId())
                    .imageFileName(findImage.getImage().getFileName())
                    .build());
        }
        return storeImageDtoList;
    }

    /**
     * 편의 시설 이미지 수정
     * @param store 해당 편의 시설
     * @param imageDtoList 수정 페이지에 입력된 수정 이미지 정보
     * @throws Exception
     */
    public void updateStoreImages(Store store, List<ModifyImageDto> imageDtoList) throws Exception {
        boolean isRepImageDelete = false;
        for (ModifyImageDto modifyImageDto : imageDtoList) {
            if(modifyImageDto.getIsDelete().equals("Y")){
                Long imageId = modifyImageDto.getImageId();
                StoreImage savedImage = storeImageRepository.findById(imageId).orElseThrow(IllegalArgumentException::new);
                if (savedImage.getImage().getIsRepresentative() == IsTrue.YES) {
                    isRepImageDelete = true;
                }
                imageService.deleteImage(store, savedImage.getImage());
                storeImageRepository.delete(savedImage);
            }
            if (modifyImageDto.getImageId() == null) {
                StoreImage newStoreImage = new StoreImage();
                newStoreImage.setStore(store);
                newStoreImage.setImage(imageService.createImage(store, modifyImageDto.getImageFile(), 1));
                storeImageRepository.save(newStoreImage);
            }
        }
        if (isRepImageDelete) {
            List<StoreImage> currentImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(store.getStoreId());
            currentImages.get(0).getImage().setIsRepresentative(IsTrue.YES);
        }
    }

    /**
     * 해당 Store의 모든 이미지 삭제
     * @param store 해당 편의 시설 엔티티 객체
     * @throws Exception
     */
    public void deleteAllStoreImage(Store store) throws Exception {
        List<StoreImage> savedStoreImages = getStoreImageListByStoreId(store.getStoreId());
        for (StoreImage savedStoreImage : savedStoreImages) {
            imageService.deleteImage(store, savedStoreImage.getImage());
            storeImageRepository.delete(savedStoreImage);
        }
    }

}
