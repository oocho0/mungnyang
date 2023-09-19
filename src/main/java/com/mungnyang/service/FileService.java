package com.mungnyang.service;

import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void uploadFile(Image image, Product product, String originalFileName, MultipartFile imageFile) throws Exception;
    void deleteFile(Product product, String name) throws Exception;

    void clearStorage(List<Image> images, String savedPath);
}
