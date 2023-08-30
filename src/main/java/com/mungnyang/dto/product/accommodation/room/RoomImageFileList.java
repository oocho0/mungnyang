package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class RoomImageFileList {
    String roomNo;
    List<MultipartFile> roomImageList;
}
