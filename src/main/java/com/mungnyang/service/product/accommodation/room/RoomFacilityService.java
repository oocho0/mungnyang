package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.dto.product.accommodation.FacilityDto;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomFacility;
import com.mungnyang.repository.product.accommodation.room.RoomFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomFacilityService {

    private final RoomFacilityRepository roomFacilityRepository;
    private final ModelMapper modelMapper;

    /**
     * 신규 RoomFacility 등록
     * @param room RoomFacility의 Room
     * @param facilityList 페이지에 입력된 RoomFacility 이름 리스트
     */
    public void saveRoomFacility(Room room, List<String> facilityList){
        for (String facilityName : facilityList) {
            RoomFacility createdRoomFacility = new RoomFacility();
            createdRoomFacility.setRoom(room);
            createdRoomFacility.setFacilityName(facilityName);
            roomFacilityRepository.save(createdRoomFacility);
        }
    }

    /**
     * 해당 Room의 RoomId로 RoomFacility 리스트 찾기
     * @param roomId 해당 방의 일련번호
     * @return 해당 방의 RoomFacility 엔티티 리스트
     */
    public List<RoomFacility> getRoomFacilityListByRoomId(Long roomId) {
        List<RoomFacility> roomFacilities = roomFacilityRepository.findByRoomRoomIdOrderByRoomFacilityId(roomId);
        if (roomFacilities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return roomFacilities;
    }

    /**
     * RoomId로 화면에 나타낼 FacilityDto List 찾기
     * @param roomId 해당 방의 일련번호
     * @return 해당 방의 방 시설 FacilityDto 리스트
     */
    public List<FacilityDto> getFacilityDtoListByRoomId(Long roomId) {
        List<RoomFacility> roomFacilityList = getRoomFacilityListByRoomId(roomId);
        List<FacilityDto> facilityDtoList = new ArrayList<>();
        for (RoomFacility roomFacility : roomFacilityList) {
            facilityDtoList.add(FacilityDto.builder()
                            .facilityId(roomFacility.getRoomFacilityId())
                            .facilityName(roomFacility.getFacilityName())
                            .includedId(roomFacility.getRoom().getRoomId())
                    .build());
        }
        return facilityDtoList;
    }
}
