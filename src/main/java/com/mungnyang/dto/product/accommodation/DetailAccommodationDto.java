package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.DetailRoomDto;
import com.mungnyang.dto.product.CommentDto;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailAccommodationDto {
    private Long id;
    private String name;
    private String category;
    private String address;
    private Double lat;
    private Double lon;
    private String checkInTime;
    private String checkOutTime;
    private String detail;
    private String status;
    private Float rateAvg;
    private Long commentCount;
    private String ownerName;
    private String ownerTel;
    private List<String> images;
    private Page<CommentDto> comments;
    private List<String> facilities;
    private List<DetailRoomDto> rooms;
}
