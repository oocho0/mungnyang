package com.mungnyang.dto.product.accommodation.room;

import com.mungnyang.dto.service.InitializeReservationRoomDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateRoomDto {

    private String roomName;
    private Integer roomPrice;
    private Integer headCount;
    private String roomDetail;
    private String roomStatus;

    private List<MultipartFile> imageList;
    private List<String> facilityList;
    private List<InitializeReservationRoomDto> reservationList;
}
