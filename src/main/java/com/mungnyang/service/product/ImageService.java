package com.mungnyang.service.product;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.ProductImage;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomImage;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreImage;
import com.mungnyang.service.FileIOService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final FileIOService fileIOService;
    private final Path path;

    /**
     * 신규 이미지 저장
     * @param product Store/Accommodation/Room 타입에 참조할 이미지 저장
     * @param imageFile 저장할 이미지 파일 MultipartFile
     * @param i 저장할 이미지의 index
     * @return 저장된 Image 엔티티
     * @throws Exception
     */
    public Image createImage(Product product, MultipartFile imageFile, int i) throws Exception {
        Image image = new Image();
        if (i == 0) {
            image.setIsRepresentative(IsTrue.YES);
        } else {
            image.setIsRepresentative(IsTrue.NO);
        }
        String originalFileName = imageFile.getOriginalFilename();
        String imageName = "";
        String imageUrl = "";
        if (!StringUtils.isEmpty(originalFileName)) {
            String savePath = getPath(product);
            imageName = fileIOService.uploadFile(savePath, originalFileName, imageFile.getBytes());
            imageUrl = "/image/" + product.getClass().getSimpleName().toLowerCase() + "/" + imageName;
        }
        image.setName(imageName);
        image.setFileName(originalFileName);
        image.setUrl(imageUrl);
        return image;
    }

    /**
     * 이미지 삭제
     * @param product Store/Accommodation/Room 타입에 참조된 이미지 삭제
     * @param image 삭제할 이미지 엔티티
     * @throws Exception
     */
    public void deleteImage(Product product, Image image) throws Exception {
        String savedPath = getPath(product);
        savedPath += "/" + image.getName();
        fileIOService.deleteFile(savedPath);
    }

    public void clearStorage(List<? extends ProductImage> productImage) {
        ProductImage firstOne = productImage.get(0);
        String savedPath = "";
        List<Image> images = new ArrayList<>();
        if (firstOne instanceof StoreImage) {
            savedPath = path.getStoreImagePath();
            List<StoreImage> convertList = (List<StoreImage>) productImage;
            for (StoreImage storeImage : convertList) {
                images.add(storeImage.getImage());
            }
        }
        if (firstOne instanceof AccommodationImage) {
            savedPath = path.getAccomImagePath();
            List<AccommodationImage> convertList = (List<AccommodationImage>) productImage;
            for (AccommodationImage accommodationImage : convertList) {
                images.add(accommodationImage.getImage());
            }
        }
        if (firstOne instanceof RoomImage) {
            savedPath = path.getRoomImagePath();
            List<RoomImage> converList = (List<RoomImage>) productImage;
            for (RoomImage roomImage : converList) {
                images.add(roomImage.getImage());
            }
        }
        fileIOService.clearStorage(images, savedPath);
    }

    /**
     * 이미지의 Path 가져오기
     * @param product
     * @return
     */
    private String getPath(Product product) {
        if (product instanceof Store) {
            return path.getStoreImagePath();
        }
        if (product instanceof Accommodation) {
            return path.getAccomImagePath();
        }
        if (product instanceof Room) {
            return path.getRoomImagePath();
        }
        return null;
    }
}
