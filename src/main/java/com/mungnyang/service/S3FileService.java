package com.mungnyang.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {
    @Value("${cloud.aws.s3.bucket}")
    private final String bucket;
    private final Path path;
    private final AmazonS3 amazonS3;

    @Override
    public void uploadFile(Image image, Product product, String originalFileName, MultipartFile imageFile) throws Exception {
        String savedPath = getPath(product);
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageFile.getSize());
        objectMetadata.setContentType(imageFile.getContentType());
        try (InputStream inputStream = imageFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, savedPath+savedFileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 요류가 발생하였습니다. (%s)", originalFileName));
        }
        image.setName(savedFileName);
        image.setFileName(originalFileName);
        image.setUrl(amazonS3.getUrl(bucket, savedPath+savedFileName).toString());
    }

    @Override
    public void deleteFile(Product product, String name) throws Exception {
        String savedPath = getPath(product);
        savedPath += "/" + name;
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, savedPath));
    }

    @Override
    public void clearStorage(List<Image> images, String savedPath) {

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
