package com.mungnyang.service.product.store;


import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.ModifyStoreDto;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreImage;
import com.mungnyang.service.fixedEntity.StateCityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StoreServiceTest {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreImageService storeImageService;

    @Autowired
    private StateCityService stateCityService;

    private CreateStoreDto testDto;
    private List<MultipartFile> testImageList;

    @Test
    @DisplayName("편의 시설 수정하기")
    @WithMockUser(username = "admin@abc.com", roles = "ADMIN")
    void 등록된_편의시설을_수정한다(){
        Store savedStore = storeService.findStoreByName(testDto.getStoreName());
        List<StoreImage> savedStoreImages = storeImageService.getStoreImageListByStore(savedStore);

        ModifyStoreDto modifyStoreDto = new ModifyStoreDto();
        modifyStoreDto.setStoreId(savedStore.getStoreId());
        modifyStoreDto.setStoreName("test1수정");
        modifyStoreDto.setSmallCategorySmallCategoryId(9L);
        modifyStoreDto.setProductAddressAddressZipcode("1234");
        modifyStoreDto.setProductAddressAddressMain("수정 주소 메인");
        modifyStoreDto.setProductAddressAddressDetail("수정 주소 상세");
        modifyStoreDto.setProductAddressAddressExtra("수정 주소 추가");
        modifyStoreDto.setProductAddressLat(34.56);
        modifyStoreDto.setProductAddressLon(67.89);
        modifyStoreDto.setStoreDetail("수정 상세 정보");
        modifyStoreDto.setStoreStatus(Status.PAUSE.name());
        List<ModifyImageDto> modifyImageDtoList = new ArrayList<>();
        int i = 0;
        for (ModifyImageDto modifyImageDto : modifyImageDtoList) {
            modifyImageDto = new ModifyImageDto();
            modifyImageDto.setImageId(savedStoreImages.get(i++).getStoreImageId());
            modifyImageDto.setIsDelete("N");
            modifyImageDto.setImageFile(null);
        }

        try {
            storeService.updateStore(savedStore.getStoreId(), modifyStoreDto, modifyImageDtoList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Store checkStore = storeService.getStoreByStoreId(savedStore.getStoreId());
        assertThat(checkStore.getStoreName()).isEqualTo(modifyStoreDto.getStoreName());
        assertThat(checkStore.getCity()).isEqualTo(stateCityService.getMatchedCity(modifyStoreDto.getProductAddressAddressZipcode()));

    }

    @Test
    @DisplayName("편의 시설 등록하기")
    @WithMockUser(username = "admin@abc.com", roles = "ADMIN")
    void 편의_시설을_등록하여_입력한_정보가_올바른지_확인한다() {
        //given - BeforeEach
        //when
        Store findStore = storeService.findStoreByName(testDto.getStoreName());
        List<StoreImage> findStoreImages = storeImageService.getStoreImageListByStore(findStore);

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
            createStoreDto.setSmallCategoryId(12L);
            createStoreDto.setProductAddressAddressZipcode(String.valueOf(12345));
            createStoreDto.setProductAddressAddressMain("my house" + i);
            createStoreDto.setProductAddressAddressDetail("somewhere" + i);
            createStoreDto.setProductAddressAddressExtra("extra" + i);
            createStoreDto.setProductAddressLat(12.3456789 + i);
            createStoreDto.setProductAddressLon(98.7654321 - i);
            createStoreDto.setStoreDetail("details of test" + i);
            createStoreDto.setStoreStatus(Status.OPEN.name());

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