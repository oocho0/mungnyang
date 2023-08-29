package com.mungnyang.service.product.store;


import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class StoreServiceTest {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreImageService storeImageService;

    private CreateStoreDto testDto;
    private List<MultipartFile> testImageList;

    @Test
    @DisplayName("편의 시설 등록하기")
    @WithMockUser(username = "admin@abc.com", roles = "ADMIN")

    void 편의_시설을_등록하여_입력한_정보가_올바른지_확인한다() {
        //given - BeforeEach
        //when
        Store findStore = storeService.findStoreByName(testDto.getStoreName());
        List<StoreImage> findStoreImages = storeImageService.findStoreImages(findStore);

        //then
        assertThat(findStore.getStoreName()).isEqualTo(testDto.getStoreName());
        assertThat(findStore.getSmallCategory().getSmallCategoryId()).isEqualTo(testDto.getSmallCategoryId());
        assertThat(findStore.getProductAddress().getAddress().getMain()).isEqualTo(testDto.getProductAddressAddressMain());
        assertThat(findStoreImages.get(0).getImage().getFileName()).isEqualTo(testImageList.get(0).getOriginalFilename());
    }

    @BeforeEach
    private void createTestStores() throws Exception {
        List<MultipartFile> multipartFileList;
        for (int i = 1; i < 6; i++) {
            CreateStoreDto createStoreDto = new CreateStoreDto();
            createStoreDto.setStoreName("test" + i);
            createStoreDto.setSmallCategoryId(1L + i);
            createStoreDto.setProductAddressAddressZipcode(String.valueOf(12345 + i * 1000));
            createStoreDto.setProductAddressAddressMain("my house" + i);
            createStoreDto.setProductAddressAddressDetail("somewhere" + i);
            createStoreDto.setProductAddressAddressExtra("extra" + i);
            createStoreDto.setProductAddressLat(String.valueOf(12.3456789 + i));
            createStoreDto.setProductAddressLat(String.valueOf(98.7654321 - i));
            createStoreDto.setStoreDetail("details of test" + i);

            List<MultipartFile> multipartFileList_this = createTestStoreImageArray(i);
            storeService.registerStore(createStoreDto, multipartFileList_this);

            if (i == 1) {
                testDto = createStoreDto;
                testImageList = multipartFileList_this;
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