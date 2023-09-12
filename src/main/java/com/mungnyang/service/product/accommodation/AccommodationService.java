package com.mungnyang.service.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.MainTopDto;
import com.mungnyang.dto.product.ResultDto;
import com.mungnyang.dto.product.SearchAccommodationFilter;
import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.accommodation.DetailAccommodationDto;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.CreateAccommodationDto;
import com.mungnyang.dto.product.accommodation.ModifyAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.DetailRoomDto;
import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.entity.Address;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.ProductAddress;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.AccommodationComment;
import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import com.mungnyang.entity.product.accommodation.AccommodationImage;
import com.mungnyang.repository.product.accommodation.AccommodationRepository;
import com.mungnyang.repository.product.accommodation.room.RoomRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final MemberService memberService;

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
                        .detail(modifyAccommodationDto.getProductAddressAddressDetail())
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
        roomService.deleteAllRooms(savedAccommodation.getAccommodationId());
        savedAccommodation.setAccommodationStatus(Status.CLOSED);
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
     * 메인 화면에 TOP3에 나올 소분류별 숙소 리스트 반환
     *
     * @return
     */
    public List<MainTopDto> getAccommodationsTopList() {
        List<MainTopDto> mainTopDtoList = new ArrayList<>();
        List<SmallCategory> accommodationCategories = categoryService.getSmallCategoryListByBigCategoryId(1L);
        for (SmallCategory accommodationCategory : accommodationCategories) {
            List<TopInfoDto> topInfoDtoList = accommodationRepository.getAccommodationTopListBySmallCategory(accommodationCategory.getSmallCategoryId());
            mainTopDtoList.add(MainTopDto.builder()
                    .name(accommodationCategory.getName())
                    .info(topInfoDtoList)
                    .build());
        }
        return mainTopDtoList;
    }

    /**
     * 메인 화면 조회 결과 리스트 반환
     *
     * @param filter 메인 화면 조회 조건(소분류, 시/군/구, 인원, 기간)
     * @return 숙소 정보 ResultDto 리스트
     */
    public List<ResultDto> getAccommodationResultList(SearchAccommodationFilter filter) {
        List<Long> categoryId = filter.getCategoryId();
        List<Long> cityId = filter.getCityId();
        Integer roomPeople = filter.getRoomPeople();
        LocalDateTime checkInDate = filter.getCheckInDate();
        LocalDateTime checkOutDate = filter.getCheckOutDate();
        return accommodationRepository.getAccommodationResultsByFilter(categoryId, cityId, roomPeople, checkInDate, checkOutDate);
    }

    public DetailAccommodationDto getAccommodationDetails(Long accommodationId) {
        Accommodation accommodation = getAccommodationByAccommodationId(accommodationId);
        Member owner = memberService.getMemberByMemberEmail(accommodation.getCreatedBy());
        List<AccommodationImage> images = accommodationImageService.getAccommodationImageListByAccommodationId(accommodationId);
        List<String> imageList = new ArrayList<>();
        for (AccommodationImage image : images) {
            imageList.add(image.getImage().getUrl());
        }
        Page<CommentDto> commentDtoList = accommodationCommentService.getCommentPage(accommodationId);
        List<AccommodationFacility> facilities = accommodationFacilityService.getAccommodationFacilityListByAccommodationId(accommodationId);
        List<String> facilityList = new ArrayList<>();
        for (AccommodationFacility facility : facilities) {
            facilityList.add(facility.getFacilityName());
        }
        List<DetailRoomDto> detailRoomDtoList = roomService.getRoomDetails(accommodationId);
        return DetailAccommodationDto.builder()
                .id(accommodation.getAccommodationId())
                .name(accommodation.getAccommodationName())
                .category(accommodation.getSmallCategory().getName())
                .address("(" + accommodation.getProductAddress().getAddress().getZipcode() + ") " + accommodation.getProductAddress().getAddress().getMain() + " " + accommodation.getProductAddress().getAddress().getExtra())
                .lat(accommodation.getProductAddress().getLat())
                .lon(accommodation.getProductAddress().getLon())
                .checkInTime(accommodation.getCheckInTime())
                .checkOutTime(accommodation.getCheckOutTime())
                .detail(accommodation.getAccommodationDetail())
                .status(StatusService.statusConverter(accommodation.getAccommodationStatus()))
                .rateAvg(accommodationCommentService.getRateAverage(accommodationId))
                .commentCount(accommodationCommentService.getCommentCount(accommodationId))
                .ownerName(owner.getName())
                .ownerTel(owner.getTel())
                .images(imageList)
                .comments(commentDtoList)
                .facilities(facilityList)
                .rooms(detailRoomDtoList)
                .build();
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

    public void deleteTest() {
        List<Accommodation> closedTestAccommodation = accommodationRepository.findByAccommodationStatus(Status.CLOSED);
        for (Accommodation accommodation : closedTestAccommodation) {
            roomService.deleteClosedTestAllRooms(accommodation.getAccommodationId());
            accommodationCommentService.deleteAllAccommodationComments(accommodation.getAccommodationId());
            accommodationFacilityService.deleteAllAccommodationFacilities(accommodation.getAccommodationId());
            try {
                accommodationImageService.deleteAllAccommodationImages(accommodation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            accommodationRepository.delete(accommodation);
        }
    }

}
