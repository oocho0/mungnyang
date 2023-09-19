package com.mungnyang.service;


import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.repository.product.accommodation.AccommodationImageRepository;
import com.mungnyang.service.product.accommodation.AccommodationImageService;
import com.mungnyang.service.product.accommodation.room.RoomImageService;
import com.mungnyang.service.product.store.StoreImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class FileIOServiceTest {

    @Autowired
    private FileService fileService;
    @Autowired
    private AccommodationImageRepository accommodationImageRepository;
    @Autowired
    private Path path;
    @Autowired
    private AccommodationImageService accommodationImageService;
    @Autowired
    private RoomImageService roomImageService;
    @Autowired
    private StoreImageService storeImageService;

    @Test
    void 고아_이미지_파일_삭제(){
        log.info("테스트 시작");
        List<AccommodationImage> allAccomImage = accommodationImageRepository.findAll();
        String savedPath = path.getAccomImagePath();
        List<Image> images = new ArrayList<>();
        for (AccommodationImage image : allAccomImage) {
            images.add(image.getImage());
        }
        File directory = new File(savedPath);
        if (!directory.isDirectory()) {
            log.info("유효하지 않은 디렉토리입니다.");
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            log.info("디렉토리가 비였습니다.");
            return;
        }
        for (File file : files) {
            boolean isExist = false;
            for (Image image : images) {
                if (file.getName().equals(image.getName())) {
                    log.info("-----------------------------------------------------------------");
                    log.info("파일 디렉토리에서 이름 : {}", file.getName());
                    log.info("파일 DB에서 이름 : {}", image.getName());
                    log.info("-----------------------------------------------------------------");
                    isExist = true;
                }
            }
            if (!isExist) {
                log.info("파일 디렉토리 Path : {}", file.getPath());
            }
        }
    }

    @Test
    void clear(){
        accommodationImageService.clearStorage();
        roomImageService.clearStorage();
        storeImageService.clearStorage();
    }
}