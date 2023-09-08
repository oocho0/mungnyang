package com.mungnyang.service;

import com.mungnyang.entity.product.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileIOService {

    /**
     * 이미지를 해당 Path에 저장하기
     * @param uploadPath 업로드될 Path
     * @param originalFileName 파일의 원래 이름
     * @param fileData 이미지 파일의 바이트 정보
     * @return 저장된 파일의 UUID 이름
     * @throws Exception
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    /**
     * 해당 Path의 이미지 파일 삭제하기
     * @param filePath 삭제할 이미지의 path
     * @throws Exception
     */
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
            return;
        }
        log.info("파일이 존재하지 않습니다.");
    }

    public void clearStorage(List<Image> images, String savedPath) {
        log.info("스토리지 검사 시작 ------");
        File directory = new File(savedPath);
        if (!directory.isDirectory()) {
            throw new RuntimeException("유효하지 않은 디렉토리입니다.");
        }
        File[] files = directory.listFiles();
        if (files == null) {
            log.info("디렉토리가 비여있습니다.");
            return;
        }
        for (File file : files) {
            boolean isExist = false;
            for (Image image : images) {
                if (file.getName().equals(image.getName())) {
                    isExist = true;
                }
            }
            if (!isExist){
                try {
                    log.info("고아 파일을 지웁니다. 파일 이름 = {}", file.getName());
                    deleteFile(file.getPath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
