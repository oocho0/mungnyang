package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.DetailRoomDto;
import com.mungnyang.dto.service.InitializeReservationRoomDto;
import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.dto.product.accommodation.room.ModifyRoomDto;
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
            reservationRoomService.saveReservationRoom(createdRoom, initializeReservationRoomDtoList);
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
                .roomPeople(savedRoom.getRoomPeople())
                .roomDetail(savedRoom.getRoomDetail())
                .roomStatus(StatusService.statusConverter(savedRoom.getRoomStatus()))
                .imageList(roomImageService.getModifyImageDtoListByRoomId(roomId))
                .facilityList(roomFacilityService.getFacilityDtoListByRoomId(roomId))
                .build();
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
        savedRoom.setRoomPeople(modifyRoomDto.getRoomPeople());
        savedRoom.setRoomDetail(modifyRoomDto.getRoomDetail());
        savedRoom.setRoomStatus(StatusService.statusConverter(modifyRoomDto.getRoomStatus()));
        roomImageService.updateRoomImage(savedRoom, modifyRoomDto.getImageList());
        roomFacilityService.updateRoomFacility(savedRoom, modifyRoomDto.getFacilityList());
        reservationRoomService.updateReservationRoom(savedRoom, modifyRoomDto.getReservationList());
    }

    public List<DetailRoomDto> getRoomDetails(Long accommodationId) {
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
                    .people(room.getRoomPeople())
                    .detail(room.getRoomDetail())
                    .status(StatusService.statusConverter(room.getRoomStatus()))
                    .images(imageList)
                    .facilities(facilityList)
                    .build());
        }
        return detailRoomDtos;
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
