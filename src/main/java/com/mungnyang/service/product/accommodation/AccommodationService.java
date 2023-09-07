package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.CreateAccommodationDto;
import com.mungnyang.dto.product.accommodation.ModifyAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.entity.Address;
import com.mungnyang.entity.product.ProductAddress;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.repository.product.accommodation.AccommodationRepository;
import com.mungnyang.repository.product.accommodation.room.RoomRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.product.StatusService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccommodationService {

    private final ModelMapper modelMapper;
    private final AccommodationRepository accommodationRepository;
    private final StateCityService stateCityService;
    private final CategoryService categoryService;
    private final AccommodationImageService accommodationImageService;
    private final AccommodationFacilityService accommodationFacilityService;
    private final AccommodationCommentService accommodationCommentService;
    private final RoomService roomService;

    private final RoomRepository repository;

    /**
     * 신규 숙소 등록하기
     *
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @throws Exception
     */
    public void registerAccommodation(CreateAccommodationDto createAccommodationDto) throws Exception {
        Accommodation accommodation = createAccommodation(createAccommodationDto);
        accommodationRepository.save(accommodation);
        accommodationImageService.saveAccommodationImages(accommodation, createAccommodationDto.getImageList());
        accommodationFacilityService.saveAccommodationFacilities(accommodation, createAccommodationDto.getFacilityList());
        roomService.saveRoomList(accommodation, createAccommodationDto.getRoomList());
    }

    /**
     * 숙소 관리 페이지에 나타낼 숙소 정보 가져오기
     *
     * @param email 로그인한 회원의 아이디
     * @return ListAccommodationDto 리스트
     */
    @Transactional(readOnly = true)
    public List<ListAccommodationDto> getListAccommodationDtoListByCreatedBy(String email) {
        List<ListAccommodationDto> listAccommodationDtos = accommodationRepository.findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(email);
        for (ListAccommodationDto listAccommodationDto : listAccommodationDtos) {
            List<ListRoomDto> roomDtoList = roomService.getListRoomDtoListByListAccommodationDto(listAccommodationDto);
            listAccommodationDto.setRooms(roomDtoList);
        }
        return listAccommodationDtos;
    }

    /**
     * 수정할 숙소 정보 가져오기
     *
     * @param accommodationId 수정할 숙소 일련번호
     * @return ModifyAccommodationDto
     */
    public ModifyAccommodationDto getModifyAccommodationDtoByAccommodationId(Long accommodationId) {
        Accommodation findAccommodation = getAccommodationByAccommodationId(accommodationId);
        modelMapper.typeMap(Accommodation.class, ModifyAccommodationDto.class).addMappings(mapping -> {
            mapping.using((Converter<Status, String>) context -> StatusService.statusConverter(context.getSource())).map(Accommodation::getAccommodationStatus, ModifyAccommodationDto::setAccommodationStatus);
            mapping.skip(ModifyAccommodationDto::setImageList);
            mapping.skip(ModifyAccommodationDto::setFacilityList);
        });
        ModifyAccommodationDto modifyAccommodationDto = modelMapper.map(findAccommodation, ModifyAccommodationDto.class);
        modifyAccommodationDto.setImageList(accommodationImageService.getModifyImageDtoListByAccommodationId(accommodationId));
        modifyAccommodationDto.setFacilityList(accommodationFacilityService.getFacilityDtoListByAccommodationId(accommodationId));
        return modifyAccommodationDto;
    }

    /**
     * 숙소 정보 수정
     *
     * @param accommodationId        수정할 숙소 일련번호
     * @param modifyAccommodationDto 수정할 숙소의 정보
     * @throws Exception
     */
    public void updateAccommodation(Long accommodationId, ModifyAccommodationDto modifyAccommodationDto) throws Exception {
        Accommodation savedAccommodation = getAccommodationByAccommodationId(accommodationId);
        savedAccommodation.setAccommodationName(modifyAccommodationDto.getAccommodationName());
        savedAccommodation.setSmallCategory(categoryService.getSmallCategoryBySmallCategoryId(modifyAccommodationDto.getSmallCategorySmallCategoryId()));
        savedAccommodation.setCity(stateCityService.getMatchedCity(modifyAccommodationDto.getProductAddressAddressZipcode()));
        savedAccommodation.setProductAddress(ProductAddress.builder()
                .address(Address.builder()
                        .zipcode(modifyAccommodationDto.getProductAddressAddressZipcode())
                        .main(modifyAccommodationDto.getProductAddressAddressMain())
                        .detail(modifyAccommodationDto.getAccommodationDetail())
                        .extra(modifyAccommodationDto.getProductAddressAddressExtra())
                        .build())
                .Lon(modifyAccommodationDto.getProductAddressLon())
                .Lat(modifyAccommodationDto.getProductAddressLat())
                .build());
        savedAccommodation.setCheckInTime(modifyAccommodationDto.getCheckInTime());
        savedAccommodation.setCheckOutTime(modifyAccommodationDto.getCheckOutTime());
        savedAccommodation.setAccommodationStatus(StatusService.statusConverter(modifyAccommodationDto.getAccommodationStatus()));
        accommodationImageService.updateAccommodationImages(savedAccommodation, modifyAccommodationDto.getImageList());
        accommodationFacilityService.updateAccommodationFacilities(savedAccommodation, modifyAccommodationDto.getFacilityList());
    }

    public void deleteAccommodation(Long accommodationId) throws Exception {
        Accommodation savedAccommodation = getAccommodationByAccommodationId(accommodationId);
        savedAccommodation.setAccommodationStatus(Status.CLOSED);
        roomService.deleteAllRooms(savedAccommodation.getAccommodationId());
    }

    /**
     * 현재 접속 회원의 아이디와 해당 숙소의 작성자가 동일하지 않으면 문제가 있음
     *
     * @param accommodationId 해당 숙소의 일련번호
     * @param email           현재 접속 회원의 아이디이자 이메일
     * @return 동일하지 않으면 true, 동일하면 문제없으므로 false
     */
    public boolean isNotWrittenByPrinciple(Long accommodationId, String email) {
        Accommodation savedAccommodation = getAccommodationByAccommodationId(accommodationId);
        return !email.equals(savedAccommodation.getCreatedBy());
    }

    /**
     * 숙소 생성
     *
     * @param createAccommodationDto 페이지에 입력한 숙소 정보
     * @return 생성된 Accommodation 엔티티
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

    /**
     * Accommodation 이름으로 Accommodation 찾기
     *
     * @param accommodationName 해당 숙소 이름
     * @return Accommodation 엔티티 객체
     */
    public Accommodation getAccommodationByAccommodationName(String accommodationName) {
        Accommodation findAccommodation = accommodationRepository.findByAccommodationNameAndAccommodationStatusNot(accommodationName, Status.CLOSED);
        if (findAccommodation == null) {
            throw new IllegalArgumentException();
        }
        return findAccommodation;
    }

    /**
     * AccommodationId로 Accommodation 객체 찾기
     *
     * @param accommodationId 찾을 Accommodation의 일련번호
     * @return Accommodation 엔티티
     */
    public Accommodation getAccommodationByAccommodationId(Long accommodationId) {
        Accommodation findAccommodation = accommodationRepository.findByAccommodationIdAndAccommodationStatusNot(accommodationId, Status.CLOSED);
        if (findAccommodation == null) {
            throw new IllegalArgumentException();
        }
        return findAccommodation;
    }
}
