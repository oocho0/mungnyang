package com.mungnyang.service;

import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final FileIOService fileIOService;
    private final Path path;

    public Image createImage(Product product, MultipartFile imageFile, int i) throws Exception {
        Image image = new Image();
        if (i == 0) {
            image.setIsRepresentative("Y");
        } else {
            image.setIsRepresentative("N");
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
            imageUrl = "/image/" + product.getClass().getName().toLowerCase() + "/" + imageName;
        }
        image.setName(imageName);
        image.setFileName(originalFileName);
        image.setUrl(imageUrl);
        return image;
    }
}
