package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateAccommodationDto {

    private String accommodationName;

    private Long smallCategoryId;

    private String productAddressAddressZipcode;
    private String productAddressAddressMain;
    private String productAddressAddressDetail;
    private String productAddressAddressExtra;
    private Double productAddressLon;
    private Double productAddressLat;

    private String accommodationDetail;
    private String checkInTime;
    private String checkOutTime;
    private String accommodationStatus;
    private List<MultipartFile> imageList;
    private List<String> facilityList;
    private List<CreateRoomDto> roomList;
}