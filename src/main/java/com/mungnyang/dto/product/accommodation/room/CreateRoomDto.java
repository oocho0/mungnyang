package com.mungnyang.dto.product.accommodation.room;

import com.mungnyang.dto.product.accommodation.FacilityDto;
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

    private List<MultipartFile> imageFile;
    private List<FacilityDto> facilityList;
}
