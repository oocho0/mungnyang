package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.CreateAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomFacility;
import com.mungnyang.entity.product.accommodation.room.RoomImage;
import com.mungnyang.service.product.accommodation.room.RoomFacilityService;
import com.mungnyang.service.product.accommodation.room.RoomImageService;
import com.mungnyang.service.product.accommodation.room.RoomService;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AccommodationServiceTest {

    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private AccommodationImageService accommodationImageService;
    @Autowired
    private AccommodationFacilityService accommodationFacilityService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomFacilityService roomFacilityService;
    @Autowired
    private RoomImageService roomImageService;

    private CreateAccommodationDto testDto;
    private List<MultipartFile> testImageList;
    private List<String> testFacilityList;
    private List<CreateRoomDto> testRoomList;

    @Test
    @DisplayName("숙소 등록하기")
    @WithMockUser(username = "seller@abc.com", roles = "SELLER")
    void 숙소를_등록하여_입력한_정보가_올바른지_확인한다() {
        //given - BeforeEach
        //when
        Accommodation findAccommodation = accommodationService.getAccommodationByAccommodationName(testDto.getAccommodationName());
        List<AccommodationImage> findAccommodationImages = accommodationImageService.getAccommodationImageListByAccommodationId(findAccommodation.getAccommodationId());
        List<AccommodationFacility> findAccommodationFacilities = accommodationFacilityService.getAccommodationFacilityListByAccommodationId(findAccommodation.getAccommodationId());
        List<Room> findRooms = roomService.getRoomListByAccommodationId(findAccommodation.getAccommodationId());
        List<RoomFacility> roomFacilities = roomFacilityService.getRoomFacilityListByRoomId(findRooms.get(0).getRoomId());
        List<RoomImage> roomImages = roomImageService.getRoomImageListByRoomId(findRooms.get(0).getRoomId());

        //then
        assertThat(findAccommodation.getAccommodationName()).isEqualTo(testDto.getAccommodationName());
        assertThat(findAccommodation.getSmallCategory().getSmallCategoryId()).isEqualTo(testDto.getSmallCategoryId());
        assertThat(findAccommodation.getProductAddress().getAddress().getMain()).isEqualTo(testDto.getProductAddressAddressMain());
        assertThat(findAccommodationImages.get(0).getImage().getFileName()).isEqualTo(testImageList.get(0).getOriginalFilename());
        assertThat(findAccommodationFacilities.get(0).getFacilityName()).isEqualTo(testFacilityList.get(0));
        assertThat(findRooms.get(0).getRoomName()).isEqualTo(testRoomList.get(0).getRoomName());
        assertThat(roomFacilities.get(0).getFacilityName()).isEqualTo(testRoomList.get(0));
        assertThat(roomImages.get(0).getImage().getFileName()).isEqualTo(testRoomList.get(0).getImageList().get(0).getOriginalFilename());
    }

    @BeforeEach
    private void createTestAccommodation() throws Exception {
        List<MultipartFile> multipartFileList;
        for (int i = 1; i < 6; i++) {
            CreateAccommodationDto createAccommodationDto = new CreateAccommodationDto();
            createAccommodationDto.setAccommodationName("test" + i);
            createAccommodationDto.setSmallCategoryId(1L);
            createAccommodationDto.setProductAddressAddressZipcode(String.valueOf(12345));
            createAccommodationDto.setProductAddressAddressMain("my house" + i);
            createAccommodationDto.setProductAddressAddressDetail("somewhere" + i);
            createAccommodationDto.setProductAddressAddressExtra("extra" + i);
            createAccommodationDto.setProductAddressLat(12.3456789 + i);
            createAccommodationDto.setProductAddressLon(98.7654321 - i);
            createAccommodationDto.setAccommodationDetail("details of test" + i);
            createAccommodationDto.setAccommodationStatus(Status.OPEN.name());

            List<MultipartFile> multipartFileList_this = createTestStoreImageArray(i);
            List<String> facilityDtos = createAccommodationFacilityList(i);
            List<CreateRoomDto> roomDtos = createRoomList(i);
            createAccommodationDto.setImageList(multipartFileList_this);
            createAccommodationDto.setFacilityList(facilityDtos);
            createAccommodationDto.setRoomList(roomDtos);
            accommodationService.registerAccommodation(createAccommodationDto);

            if (i == 1) {
                testDto = createAccommodationDto;
                testImageList = multipartFileList_this;
                testFacilityList = facilityDtos;
                testRoomList = roomDtos;
            }

        }
    }
    private List<CreateRoomDto> createRoomList(int i) {
        List<CreateRoomDto> createRoomDtos = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            CreateRoomDto createRoomDto = new CreateRoomDto();
            createRoomDto.setRoomName("test" + i + "room" + j);
            createRoomDto.setRoomPrice(2000 * i * j);
            createRoomDto.setRoomPeople(i);
            createRoomDto.setRoomDetail("detail of test " + i + "room" + j);
            createRoomDto.setRoomStatus(Status.OPEN.name());
            List<String> facilityDtoList = createRoomFacility(i, j);
            createRoomDto.setFacilityList(facilityDtoList);
            List<MultipartFile> roomImageDtoList = createTestStoreImageArray(i);
            createRoomDto.setImageList(roomImageDtoList);
            createRoomDtos.add(createRoomDto);
        }
        return createRoomDtos;
    }

    private List<String> createRoomFacility(int i, int j) {
        List<String> facilityDtoList = new ArrayList<>();
        for (int k = 0; k < 6; k++) {
            facilityDtoList.add("test" + i + "room"+ j + "facility" + k);
        }
        return facilityDtoList;
    }

    private List<String> createAccommodationFacilityList(int i) {
        List<String> facilityDtos = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            facilityDtos.add("test" + i + "facility" + j);
        }
        return facilityDtos;
    }

    private List<MultipartFile> createTestStoreImageArray(int i) {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int j = 1; j < 6; j++) {
            String path = "/Users/juheekim/Desktop/coding/mycodesource/Back_end/Spring/shop/image";
            String imageName = "test" + i + "Image" + j + ".jpg";
            MockMultipartFile mockMultipartFile = new MockMultipartFile(path, imageName, "image/jpg",
                    new byte[]{(byte) (i * j), (byte) (2 * i * j), (byte) (3 * i * j), (byte) (4 * i * j)});
            multipartFileList.add(mockMultipartFile);
        }
        return multipartFileList;
    }

}