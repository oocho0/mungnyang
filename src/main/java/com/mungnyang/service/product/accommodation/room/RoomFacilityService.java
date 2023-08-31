package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.accommodation.room.RoomFacilityDto;
import com.mungnyang.entity.product.Facility;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomFacility;
import com.mungnyang.repository.product.accommodation.room.RoomFacilityRepository;
import com.mungnyang.service.product.StatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param roomFacilityList 페이지에 입력된 Room Facility 정보
     */
    public void saveRoomFacility(Room room, List<RoomFacilityDto> roomFacilityList){
        for (RoomFacilityDto roomFacilityDto : roomFacilityList) {
            modelMapper.typeMap(RoomFacilityDto.class, Facility.class).addMappings(mapping -> {
                mapping.using((Converter<String, IsTrue>) ctx -> StatusService.isTrueConverter(ctx.getSource()))
                        .map(RoomFacilityDto::getFacilityIsExist, Facility::setIsExist);
                mapping.map(RoomFacilityDto::getFacilityName, Facility::setName);
            });
            Facility facility = modelMapper.map(roomFacilityDto, Facility.class);
            RoomFacility createdRoomFacility = new RoomFacility();
            createdRoomFacility.setRoom(room);
            createdRoomFacility.setFacility(facility);
            roomFacilityRepository.save(createdRoomFacility);
        }
    }

    public List<RoomFacility> findRoomFacilityByRoom(Room room) {
        List<RoomFacility> roomFacilities = roomFacilityRepository.findByRoomRoomIdOrderByRoomFacilityId(room.getRoomId());
        if (roomFacilities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return roomFacilities;
    }
}
