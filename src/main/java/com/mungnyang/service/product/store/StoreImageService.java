package com.mungnyang.service.product.store;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.store.StoreImageDto;
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
     *
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
     * @param store 찾을 Store
     * @return 해당 Store의 이미지 List
     */
    public List<StoreImage> getStoreImageListByStore(Store store) {
        List<StoreImage> findImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(store.getStoreId());
        if (findImages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return findImages;
    }

    /**
     * 수정 페이지에 출력될 StoreImageDto 리스트 반환
     * @param storeId 찾을 Store의 일련번호
     * @return Store의 이미지 리스트 반환
     */
    public List<StoreImageDto> getStoreImageDtoListByStoreId(Long storeId) {
        List<StoreImage> findImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(storeId);
        List<StoreImageDto> storeImageDtoList = new ArrayList<>();
        for (StoreImage findImage : findImages) {
            storeImageDtoList.add(modelMapper.map(findImage, StoreImageDto.class));
        }
        return storeImageDtoList;
    }

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

    public void deleteAllStoreImage(Store store) throws Exception {
        List<StoreImage> savedStoreImages = getStoreImageListByStore(store);
        for (StoreImage savedStoreImage : savedStoreImages) {
            imageService.deleteImage(store, savedStoreImage.getImage());
            storeImageRepository.delete(savedStoreImage);
        }
    }

}
