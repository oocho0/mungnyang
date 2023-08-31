package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateRoomDto {

    private String roomName;
    private Integer roomPrice;
    private String roomDetail;
    private String roomStatus;
    private String isAvailable;

    private List<MultipartFile> roomImageFile;
    private List<RoomFacilityDto> roomFacilityList;
}
