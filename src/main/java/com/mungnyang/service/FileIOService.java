package com.mungnyang.service;

import com.mungnyang.constant.Path;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.store.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class FileIOService implements FileService {

    private final Path path;

    /**
     * 이미지를 해당 Path에 저장하기
     * @param product 업로드될 Path
     * @param originalFileName 파일의 원래 이름
     * @param imageFile 이미지 파일의 바이트 정보
     * @return 저장된 파일의 UUID 이름
     * @throws Exception
     */
    @Override
    public void uploadFile(Image image, Product product, String originalFileName, MultipartFile imageFile) throws Exception{
        String savedPath = getPath(product);
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = savedPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(imageFile.getBytes());
        fos.close();
        image.setName(savedFileName);
        image.setFileName(originalFileName);
        image.setUrl("/images/" + product.getClass().getSimpleName().toLowerCase() + "/" + savedFileName);
    }

    /**
     * 해당 Path의 이미지 파일 삭제하기
     * @param product Store/Accommodation/Room 타입에 참조된 이미지 삭제
     * @param name 삭제할 이미지의 name
     * @throws Exception
     */
    @Override
    public void deleteFile(Product product, String name) throws Exception{
        String savedPath = getPath(product);
        savedPath += "/" + name;
        deleteFile(savedPath);
    }

    private void deleteFile(String savedPath) {
        File deleteFile = new File(savedPath);
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
