package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.dto.product.accommodation.ModifyFacilityDto;
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
    public List<ModifyFacilityDto> getFacilityDtoListByRoomId(Long roomId) {
        List<RoomFacility> roomFacilityList = getRoomFacilityListByRoomId(roomId);
        List<ModifyFacilityDto> modifyFacilityDtoList = new ArrayList<>();
        for (RoomFacility roomFacility : roomFacilityList) {
            modifyFacilityDtoList.add(ModifyFacilityDto.builder()
                            .facilityId(roomFacility.getRoomFacilityId())
                            .facilityName(roomFacility.getFacilityName())
                    .build());
        }
        return modifyFacilityDtoList;
    }

    /**
     * 방 시설 정보 수정하기
     * @param room 해당 방
     * @param facilityList 수정할 방 정보 리스트
     */
    public void updateRoomFacility(Room room, List<ModifyFacilityDto> facilityList) {
        for (ModifyFacilityDto modifyFacilityDto : facilityList) {
            if (modifyFacilityDto.getIsDelete().equals("Y")) {
                Long facilityId = modifyFacilityDto.getFacilityId();
                RoomFacility savedFacility = getRoomFacilityByRoomFacilityId(facilityId);
                roomFacilityRepository.delete(savedFacility);
                continue;
            }
            if (modifyFacilityDto.getFacilityId() == null) {
                RoomFacility newFacility = new RoomFacility();
                newFacility.setFacilityName(modifyFacilityDto.getFacilityName());
                newFacility.setRoom(room);
                roomFacilityRepository.save(newFacility);
            }
        }
    }

    /**
     * 모든 방 시설 삭제 하기
     * @param roomId 해당 방 일련번호
     */
    public void deleteAllRoomFacilities(Long roomId) {
        List<RoomFacility> savedFacilities = getRoomFacilityListByRoomId(roomId);
        for (RoomFacility savedFacility : savedFacilities) {
            roomFacilityRepository.delete(savedFacility);
        }
    }

    /**
     * 방 시설 번호로 방 시설 찾기
     * @param roomFacilityId 해당 방 일련번호
     * @return RoomFacility 엔티티
     */
    private RoomFacility getRoomFacilityByRoomFacilityId(Long roomFacilityId) {
        return roomFacilityRepository.findById(roomFacilityId).orElseThrow(IllegalArgumentException::new);
    }
}
