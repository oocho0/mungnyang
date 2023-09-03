package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.FacilityDto;
import com.mungnyang.dto.product.accommodation.CreateAccommodationDto;
import com.mungnyang.dto.product.accommodation.ModifyAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.repository.product.accommodation.AccommodationRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.member.MemberService;
import com.mungnyang.service.product.StatusService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final ModelMapper modelMapper;
    private final AccommodationRepository accommodationRepository;
    private final CategoryService categoryService;
    private final StateCityService stateCityService;
    private final AccommodationImageService accommodationImageService;
    private final AccommodationFacilityService accommodationFacilityService;
    private final RoomService roomService;

    /**
     * 숙소 등록 시 필요한 소분류를 모델에 담아 전달
     * @param model 전달할 모델
     */
    public void initializeStore(Model model) {
        List<SmallCategory> smallCategoryList = categoryService.getSmallCategoriesWithOutThisBigCategoryId(1L);
        model.addAttribute("smallCategories", smallCategoryList);
        String[] accommodationFacility = {"24시간 리셉션", "반려견 운동장", "반려견 수영장", "주차", "무료 WiFi", "반려견 셀프 목욕", "조식"};
        model.addAttribute("accommodationFacility", accommodationFacility);
        String[] roomFacility = {"객실 무료 WiFi", "에어컨", "개별 바베큐", "스파", "반려견 욕실 용품", "반려견 드라이룸", "냉장고"};
        model.addAttribute("roomFacility", roomFacility);
    }

    /**
     * Accommodation 이름으로 Accommodation 찾기
     * @param accommodationName
     * @return
     */
    public Accommodation findAccommodationByName(String accommodationName) {
        Accommodation findAccommodation = accommodationRepository.findByAccommodationName(accommodationName);
        if (findAccommodation == null) {
            throw new IllegalStateException();
        }
        return findAccommodation;
    }

    /**
     * 신규 숙소 등록하기
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @param accommodationImageFileList 페이지에 입력한 숙소 이미지 리스트
     * @param accommodationFacilityList 페이지에 입력한 숙소 시설 리스트
     * @param roomList 페이지에 입력한 방 정보 리스트
     * @throws Exception
     */
    public void registerAccommodation(CreateAccommodationDto createAccommodationDto,
                                      List<MultipartFile> accommodationImageFileList,
                                      List<FacilityDto> accommodationFacilityList,
                                      List<CreateRoomDto> roomList) throws Exception {
        Accommodation accommodation = createAccommodation(createAccommodationDto);
        accommodationRepository.save(accommodation);
        accommodationImageService.saveAccommodationImages(accommodation, accommodationImageFileList);
        accommodationFacilityService.saveAccommodationFacilities(accommodation, accommodationFacilityList);
        roomService.saveRoom(accommodation, roomList);
    }

    /**
     * 숙소 관리 페이지에 나타낼 숙소 정보 가져오기
     * @param email 로그인한 회원의 아이디
     * @return List<숙소 정보>
     */
    @Transactional(readOnly = true)
    public List<ListAccommodationDto> getAccommodationsForList(String email) {
        List<ListAccommodationDto> listAccommodationDtos =  accommodationRepository.findListAccommodationDtoByCreatedByOrderByReqDateDesc(email);
        for (ListAccommodationDto listAccommodationDto : listAccommodationDtos) {
            List<ListRoomDto> roomDtoList = roomService.findRoomListInfoByAccommodation(listAccommodationDto);
            listAccommodationDto.setRooms(roomDtoList);
        }
        return listAccommodationDtos;
    }

    public ModifyAccommodationDto findAccommodationByAccommodationId(Long accommodationId) {
        ModifyAccommodationDto modifyAccommodationDto = getAccommodation(accommodationId);
        modifyAccommodationDto.setAccommodationImageDtoList(accommodationImageService.findAccommodationImageDtos(accommodationId));
        return modifyAccommodationDto;
    }

    private ModifyAccommodationDto getAccommodation(Long accommodationId) {
        Accommodation findAccommodation = accommodationRepository.findById(accommodationId).orElseThrow(IllegalArgumentException::new);
        modelMapper.typeMap(Accommodation.class, ModifyAccommodationDto.class).addMappings(mapping -> {
            mapping.using((Converter<Status, String>) context -> StatusService.statusConverter(context.getSource())).map(Accommodation::getAccommodationStatus, ModifyAccommodationDto::setAccommodationStatus);
            mapping.skip(ModifyAccommodationDto::setAccommodationImageDtoList);
        });
        return modelMapper.map(findAccommodation, ModifyAccommodationDto.class);
    }

    /**
     * 숙소 생성
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @return 생성된 숙소 객체
     */
    private Accommodation createAccommodation(CreateAccommodationDto createAccommodationDto) {
        modelMapper.typeMap(CreateAccommodationDto.class, Accommodation.class).addMappings(mapping -> {
            mapping.using((Converter<String, Status>) ctx -> StatusService.statusConverter(ctx.getSource())).map(CreateAccommodationDto::getAccommodationStatus, Accommodation::setAccommodationStatus);
            mapping.skip(Accommodation::setAccommodationId);
            mapping.skip(Accommodation::setCity);
        });
        Accommodation createdAccommodation = modelMapper.map(createAccommodationDto, Accommodation.class);
        createdAccommodation.setCity(stateCityService.getMatchedCity(createAccommodationDto.getProductAddressAddressZipcode()));
        return createdAccommodation;
    }
}
