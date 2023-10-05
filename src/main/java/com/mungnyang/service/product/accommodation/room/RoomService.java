package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.service.CreateReservationRoomDto;
import com.mungnyang.dto.service.CreateReservationRoomDtoWithRoom;
import com.mungnyang.dto.service.ReservationInfoDto;
import com.mungnyang.dto.product.accommodation.room.*;
import com.mungnyang.dto.service.InitializeReservationRoomDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomFacility;
import com.mungnyang.entity.product.accommodation.room.RoomImage;
import com.mungnyang.repository.product.accommodation.room.RoomRepository;
import com.mungnyang.service.product.StatusService;
import com.mungnyang.service.service.ReservationRoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final RoomImageService roomImageService;
    private final RoomFacilityService roomFacilityService;
    private final ReservationRoomService reservationRoomService;

    /**
     * 신규 숙소 등록 화면에서 온 정보로 Room, RoomFacility, RoomImage 신규 저장
     *
     * @param accommodation Room 참조할 Accommodation
     * @param roomList      페이지에 입력된 Room 정보 리스트
     */
    public void saveRoomList(Accommodation accommodation, List<CreateRoomDto> roomList) throws Exception {
        for (CreateRoomDto createRoomDto : roomList) {
            saveRoom(accommodation, createRoomDto);
        }
    }

    /**
     * 하나의 Room의 RoomFacility들, RoomImage들 저장
     *
     * @param accommodation 해당 숙소
     * @param createRoomDto 페이지에 입력된 Room 정보 리스트
     * @throws Exception
     */
    public void saveRoom(Accommodation accommodation, CreateRoomDto createRoomDto) throws Exception {
        Room createdRoom = createdRoom(accommodation, createRoomDto);
        roomRepository.save(createdRoom);
        List<MultipartFile> roomImageFileList = createRoomDto.getImageList();
        roomImageService.saveRoomImages(createdRoom, roomImageFileList);
        List<String> roomFacilityList = createRoomDto.getFacilityList();
        roomFacilityService.saveRoomFacility(createdRoom, roomFacilityList);
        List<InitializeReservationRoomDto> initializeReservationRoomDtoList = createRoomDto.getReservationList();
        if (initializeReservationRoomDtoList != null) {
            reservationRoomService.saveReservationRoomForInitialize(createdRoom, initializeReservationRoomDtoList);
        }
    }

    /**
     * 신규 Room 객체 생성
     *
     * @param accommodation Room 참조할 Accommodation
     * @param createRoomDto 페이지에 입력된 Room 정보
     * @return 생성된 Room 객체
     */
    private Room createdRoom(Accommodation accommodation, CreateRoomDto createRoomDto) {
        modelMapper.typeMap(CreateRoomDto.class, Room.class).addMappings(mapping -> {
            mapping.using((Converter<String, Status>) ctx -> StatusService.statusConverter(ctx.getSource()))
                    .map(CreateRoomDto::getRoomStatus, Room::setRoomStatus);
            mapping.skip(Room::setRoomId);
            mapping.skip(Room::setAccommodation);
        });
        Room createdRoom = modelMapper.map(createRoomDto, Room.class);
        createdRoom.setAccommodation(accommodation);
        return createdRoom;
    }

    /**
     * ListAccommodationDto에 들어갈 Room 객체 리스트 찾기
     *
     * @param listAccommodationDto 찾을 Accommodation 객체의 숙소 리스트 화면 Dto
     * @return Room 엔티티 리스트
     */
    public List<ListRoomDto> getListRoomDtoListByListAccommodationDto(ListAccommodationDto listAccommodationDto) {
        List<Room> rooms = getRoomListByAccommodationId(listAccommodationDto.getAccommodationId());
        List<ListRoomDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            modelMapper.typeMap(Room.class, ListRoomDto.class).addMappings(mapping -> {
                mapping.using((Converter<Status, String>) ctx -> StatusService.statusConverter(ctx.getSource())).map(Room::getRoomStatus, ListRoomDto::setRoomStatus);
            });
            roomDtos.add(modelMapper.map(room, ListRoomDto.class));
        }
        return roomDtos;
    }

    /**
     * RoomId로 수정 화면에 나타낼 방 정보 찾기
     *
     * @param roomId 해당 방 일련번호
     * @return 수정 화면에 나타낼 ModifyRoomDto
     */
    public ModifyRoomDto getModifyRoomDtoByRoomId(Long roomId) {
        Room savedRoom = getRoomByRoomId(roomId);
        return ModifyRoomDto.builder()
                .accommodationId(savedRoom.getAccommodation().getAccommodationId())
                .roomId(savedRoom.getRoomId())
                .roomName(savedRoom.getRoomName())
                .roomPrice(savedRoom.getRoomPrice())
                .headCount(savedRoom.getHeadCount())
                .roomDetail(savedRoom.getRoomDetail())
                .roomStatus(StatusService.statusConverter(savedRoom.getRoomStatus()))
                .imageList(roomImageService.getModifyImageDtoListByRoomId(roomId))
                .facilityList(roomFacilityService.getFacilityDtoListByRoomId(roomId))
                .build();
    }

    /**
     * 숙소 상태 변화에 따라 방 상태도 업데이트
     * @param accommodationId 수정되는 숙소
     * @param accommodationStatus 숙소의 상태
     */
    public void updateRoomStatus(Long accommodationId, Status accommodationStatus) {
        List<Room> savedRooms = getRoomListByAccommodationId(accommodationId);
        for (Room savedRoom : savedRooms) {
            savedRoom.setRoomStatus(accommodationStatus);
        }
    }

    /**
     * 방 수정 하기
     *
     * @param roomId        해당 방 일련번호
     * @param modifyRoomDto 수정할 방 정보
     * @throws Exception
     */
    public void updateRoom(Long roomId, ModifyRoomDto modifyRoomDto) throws Exception {
        Room savedRoom = getRoomByRoomId(roomId);
        savedRoom.setRoomName(modifyRoomDto.getRoomName());
        savedRoom.setRoomPrice(modifyRoomDto.getRoomPrice());
        savedRoom.setHeadCount(modifyRoomDto.getHeadCount());
        savedRoom.setRoomDetail(modifyRoomDto.getRoomDetail());
        savedRoom.setRoomStatus(StatusService.statusConverter(modifyRoomDto.getRoomStatus()));
        roomImageService.updateRoomImage(savedRoom, modifyRoomDto.getImageList());
        roomFacilityService.updateRoomFacility(savedRoom, modifyRoomDto.getFacilityList());
        if (modifyRoomDto.getReservationList() != null) {
            reservationRoomService.updateReservationRoom(savedRoom, modifyRoomDto.getReservationList());
        }
    }

    /**
     * 숙소 디테일 화면에 보일 숙소 방 정보
     *
     * @param accommodationId 해당 숙소 일련번호
     * @param reservationInfo 숙소 예약 검색 시, 입력한 정보
     * @return 숙소 예약 검색 결과에 해당하는 숙소 방인지 아닌지 선별하는 정보가 담긴 방 점보
     */
    public List<DetailRoomDto> getRoomDetails(Long accommodationId, ReservationInfoDto reservationInfo) {
        List<Room> rooms = getRoomListByAccommodationId(accommodationId);
        List<DetailRoomDto> detailRoomDtos = new ArrayList<>();
        for (Room room : rooms) {
            List<RoomImage> roomImages = roomImageService.getRoomImageListByRoomId(room.getRoomId());
            List<String> imageList = new ArrayList<>();
            for (RoomImage roomImage : roomImages) {
                imageList.add(roomImage.getImage().getUrl());
            }
            List<RoomFacility> roomFacilities = roomFacilityService.getRoomFacilityListByRoomId(room.getRoomId());
            List<String> facilityList = new ArrayList<>();
            for (RoomFacility roomFacility : roomFacilities) {
                facilityList.add(roomFacility.getFacilityName());
            }
            detailRoomDtos.add(DetailRoomDto.builder()
                    .id(room.getRoomId())
                    .name(room.getRoomName())
                    .price(room.getRoomPrice())
                    .headCount(room.getHeadCount())
                    .detail(room.getRoomDetail())
                    .status(StatusService.statusConverter(room.getRoomStatus()))
                    .images(imageList)
                    .facilities(facilityList)
                    .isHeadCountCapable(headCountCapability(room.getHeadCount(), reservationInfo.getHeadCount()))
                    .isAvailable(roomAvailability(room.getRoomId(), reservationInfo.getCheckInDate(), reservationInfo.getCheckOutDate()))
                    .build());
        }
        return detailRoomDtos;
    }

    public List<AvailableRoomDto> getRoomAvailable(Long accommodationId, ReservationInfoDto reservationInfoDto) {
        List<AvailableRoomDto> availableRoomDtoList = new ArrayList<>();
        List<Room> rooms = getRoomListByAccommodationId(accommodationId);
        for (Room room : rooms) {
            availableRoomDtoList.add(new AvailableRoomDto(
                    room.getRoomId(),
                    headCountCapability(room.getHeadCount(), reservationInfoDto.getHeadCount()),
                    roomAvailability(room.getRoomId(), reservationInfoDto.getCheckInDate(), reservationInfoDto.getCheckOutDate())
            ));
        }
        return availableRoomDtoList;
    }

    /**
     * 검색 시 입력한 인원 수가 방의 기본 인원 이상인지 아닌지 반환
     * 검색 하지 않으면 입력한 인원 수를 0으로 두고 판단
     *
     * @param stdHeadCount   방의 최대 인원 수
     * @param inputHeadCount 입력한 인원 수
     * @return 입력한 인원 수가 방의 최대 인원 수보다 크면 YES
     */
    private String headCountCapability(Integer stdHeadCount, Integer inputHeadCount) {
        if (inputHeadCount == null) {
            inputHeadCount = 0;
        }
        if (stdHeadCount < inputHeadCount) {
            return "NO";
        }
        return "YES";
    }

    private String roomAvailability(Long roomId, LocalDateTime inputCheckInDate, LocalDateTime inputCheckOutDate) {
        List<Room> roomAvailability = roomRepository.getRoomAvailability(roomId, inputCheckInDate, inputCheckOutDate);
        if (roomAvailability.size() == 0) {
            return "NO";
        }
        return "YES";
    }

    public List<ReservationListRoomDto> getReservationListRoomDto(Long accommodationId) {
        List<Room> savedRoom = getRoomListByAccommodationId(accommodationId);
        List<ReservationListRoomDto> roomList = new ArrayList<>();
        for (Room room : savedRoom) {
            Long totalReservationCount = reservationRoomService.getTotalReservationCountForRoom(room.getRoomId());
            Long currentReservationCount = reservationRoomService.getCurrentReservationCountForRoom(room.getRoomId());
            Long pastReservationCount = totalReservationCount - currentReservationCount;
            roomList.add(ReservationListRoomDto.builder()
                    .roomId(room.getRoomId())
                    .roomName(room.getRoomName())
                    .roomStatus(StatusService.statusConverter(room.getRoomStatus()))
                    .totalReservationCount(totalReservationCount)
                    .currentReservationCount(currentReservationCount)
                    .pastReservationCount(pastReservationCount)
                    .build());
        }
        return roomList;
    }

    /**
     * 해당 숙소의 모든 방 삭제하기
     *
     * @param accommodationId 해당 숙소 일련번호
     * @throws Exception
     */
    public void deleteAllRooms(Long accommodationId) {
        List<Room> savedRooms = getRoomListByAccommodationId(accommodationId);
        for (Room savedRoom : savedRooms) {
            deleteRoom(savedRoom);
        }
    }

    /**
     * 해당 방 삭제 하기
     *
     * @param roomId 삭젝할 방 일련번호
     * @throws Exception
     */
    public void deleteRoom(Long roomId) {
        Room room = getRoomByRoomId(roomId);
        deleteRoom(room);
    }

    /**
     * 방 삭제하기
     *
     * @param savedRoom 삭제할 방
     * @throws Exception
     */
    private void deleteRoom(Room savedRoom) {
        savedRoom.setRoomStatus(Status.CLOSED);
    }

    /**
     * Url로 넘어온 accommodationId와 roomId의 accommodationId가 동일한지 판단해서 다르면 문제임
     *
     * @param accommodationId Url로 넘어온 accommodationId
     * @param roomId          Url로 넘어온 roomId
     * @return 동일하지 않으면 true, 동일하면 문제없으므로 false
     */
    public boolean isNotOneOfAccommodationRoom(Long accommodationId, Long roomId) {
        Room savedRoom = getRoomByRoomId(roomId);
        return !Objects.equals(accommodationId, savedRoom.getAccommodation().getAccommodationId());
    }

    /**
     * Accommodation 객체로 Room 객체 리스트 반환
     *
     * @return 찾은 Room 리스트 없으면 예외 발생
     */
    public List<Room> getRoomListByAccommodationId(Long accommodationId) {
        List<Room> rooms = roomRepository.findByAccommodationAccommodationIdAndRoomStatusNotOrderByRoomId(accommodationId, Status.CLOSED);
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return rooms;
    }

    public InfoRoomDto getInfoRoomDto(Long roomId) {
        Room room = getRoomByRoomId(roomId);
        Long totalReservationCount = reservationRoomService.getTotalReservationCountForRoom(room.getRoomId());
        Long currentReservationCount = reservationRoomService.getCurrentReservationCountForRoom(room.getRoomId());
        Long pastReservationCount = totalReservationCount - currentReservationCount;
        return InfoRoomDto.builder()
                .accommodationId(room.getAccommodation().getAccommodationId())
                .accommodationName(room.getAccommodation().getAccommodationName())
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .roomStatus(StatusService.statusConverter(room.getRoomStatus()))
                .totalReservationCount(totalReservationCount)
                .currentReservationCount(currentReservationCount)
                .pastReservationCount(pastReservationCount)
                .currentReservationRoomList(reservationRoomService.getCurrentReservationRoomList(room.getRoomId()))
                .pastReservationRoomList(reservationRoomService.getPastReservationRoomList(room.getRoomId()))
                .build();
    }

    /**
     * RoomId로 Room 찾기
     *
     * @param roomId 해당 방의 일련번호
     * @return 해당 방 엔티티
     */
    public Room getRoomByRoomId(Long roomId) {
        Room findRoom = roomRepository.findByRoomIdAndRoomStatusNot(roomId, Status.CLOSED);
        if (findRoom == null) {
            throw new IllegalArgumentException();
        }
        return findRoom;
    }

    /**
     * 예약-방 신규 생성을 위해 RoomId를 Room 엔티티로 바꿔서 전달해줌
     *
     * @param reservationRoomList RoomId가 들어있는 DTO
     * @return Room 엔티티로 바꾼 DTO
     */
    public List<CreateReservationRoomDtoWithRoom> getCreateReservationRoomDtoWithRoom(List<CreateReservationRoomDto> reservationRoomList) {
        List<CreateReservationRoomDtoWithRoom> createReservationRoomDtoWithRoomList = new ArrayList<>();
        for (CreateReservationRoomDto createReservationRoomDto : reservationRoomList) {
            modelMapper.typeMap(CreateReservationRoomDto.class, CreateReservationRoomDtoWithRoom.class).addMappings(mapping -> {
                mapping.skip(CreateReservationRoomDtoWithRoom::setRoom);
            });
            CreateReservationRoomDtoWithRoom createReservationRoomDtoWithRoom = modelMapper.map(createReservationRoomDto, CreateReservationRoomDtoWithRoom.class);
            createReservationRoomDtoWithRoom.setRoom(getRoomByRoomId(createReservationRoomDto.getRoomId()));
            createReservationRoomDtoWithRoomList.add(createReservationRoomDtoWithRoom);
        }
        return createReservationRoomDtoWithRoomList;
    }

    public void deleteClosedTestAllRooms(Long accommodationId) {
        List<Room> savedRooms = getAllRoomForTestDelete(accommodationId);
        for (Room savedRoom : savedRooms) {
            roomFacilityService.deleteAllRoomFacilities(savedRoom.getRoomId());
            try {
                roomImageService.deleteAllRoomImages(savedRoom);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            roomRepository.delete(savedRoom);
        }
    }

    public List<Room> getAllRoomForTestDelete(Long accommodationId) {
        List<Room> rooms = roomRepository.findByAccommodationAccommodationId(accommodationId);
        if (rooms == null) {
            throw new IllegalArgumentException();
        }
        return rooms;
    }
}
