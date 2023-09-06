package com.mungnyang.repository.product.store;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.product.store.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreRepositoryTest {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("페이징 처리")
    void 모든_Store를_찾아온다(){
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            System.out.println(store.getStoreName());
        }
    }

    @Test
    @DisplayName("페이징")
    public void 페이징처리를해서_Store를_불러온다(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> stores = storeRepository.findAll(pageable);
        System.out.println(stores.getTotalElements());
        System.out.println(stores.getTotalPages());
        for (Store store : stores) {
            System.out.println(store.getStoreName());
        }
    }


    @BeforeEach
    public void createTest(){
        for (int i = 1; i < 31; i++) {
            CreateStoreDto storeDto = new CreateStoreDto();
            storeDto.setStoreName("test"+i);
            storeDto.setStoreStatus(Status.OPEN.name());
            storeDto.setStoreDetail("StoreDetail"+i);
            storeDto.setProductAddressAddressMain("AddressMain"+i);
            storeDto.setProductAddressAddressZipcode(String.valueOf(12345+(i*100)));
            storeDto.setProductAddressAddressDetail("AddressDetail"+i);
            storeDto.setProductAddressAddressExtra("AddressExtra"+i);
            storeDto.setProductAddressLat(12.3567*(i*0.001));
            storeDto.setProductAddressLon(22.3456*(i*0.001));
            storeDto.setSmallCategoryId(9L);

            List<MultipartFile> multipartFileList_this = createTestStoreImageArray(i);
            storeDto.setImageList(multipartFileList_this);
            try {
                storeService.registerStore(storeDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private List<MultipartFile> createTestStoreImageArray(int i) {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int j = 1; j < 6; j++) {
            String path = "/Users/juheekim/Desktop/coding/mycodesource/Back_end/Spring/shop/image";
            String imageName = "test" + i + "Image" + j + ".jpg";
            MockMultipartFile mockMultipartFile = new MockMultipartFile(path, imageName, "image/jpg",
                    new byte[]{(byte) (1 * i * j), (byte) (2 * i * j), (byte) (3 * i * j), (byte) (4 * i * j)});
            multipartFileList.add(mockMultipartFile);
        }
        return multipartFileList;
    }


}