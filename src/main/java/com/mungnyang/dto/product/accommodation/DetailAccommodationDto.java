package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.DetailRoomDto;
import com.mungnyang.dto.product.store.CommentDto;
import lombok.*;

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
    private String checkInTime;
    private String checkOutTime;
    private String detail;
    private String status;
    private List<String> images;
    private List<CommentDto> comments;
    private List<String> facilities;
    private List<DetailRoomDto> rooms;
}
