package com.mungnyang.service.product.store;

import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreImage;
import com.mungnyang.repository.product.store.StoreImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreImageRepository storeImageRepository;
    private final ImageService imageService;

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
     *
     * @param store 찾을 Store
     * @return 해당 Store의 이미지 List
     */
    public List<StoreImage> findStoreImages(Store store) {
        List<StoreImage> findImages = storeImageRepository.findByStoreStoreIdOrderByStoreImageId(store.getStoreId());
        if (findImages.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return findImages;
    }

}
