package com.mungnyang.service.product;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.FileIOService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

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
     * @return 저장된 이미지 객체
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
            String savePath = "";
            if (product instanceof Store) {
                savePath = path.getStoreImagePath();
            }
            if (product instanceof Accommodation) {
                savePath = path.getAccomImagePath();
            }
            if (product instanceof Room) {
                savePath = path.getRoomImagePath();
            }
            imageName = fileIOService.uploadFile(savePath, originalFileName, imageFile.getBytes());
            imageUrl = "/image/" + product.getClass().getSimpleName().toLowerCase() + "/" + imageName;
        }
        image.setName(imageName);
        image.setFileName(originalFileName);
        image.setUrl(imageUrl);
        return image;
    }

    public void deleteImage(Product product, Image image) throws Exception {
        String savedPath = "";
        if (product instanceof Store) {
            savedPath = path.getStoreImagePath();
        }
        if (product instanceof Accommodation) {
            savedPath = path.getAccomImagePath();
        }
        if (product instanceof Room) {
            savedPath = path.getRoomImagePath();
        }
        savedPath += "/" + image.getName();
        fileIOService.deleteFile(savedPath);
    }
}
