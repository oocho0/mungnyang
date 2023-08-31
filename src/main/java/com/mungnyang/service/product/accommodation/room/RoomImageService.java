package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomImage;
import com.mungnyang.repository.product.accommodation.room.RoomImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomImageService {

    private final RoomImageRepository roomImageRepository;
    private final ImageService imageService;

    /**
     * 신규 RoomImage 저장
     * @param room RoomImage의 Room 객체
     * @param roomImageFileList 페이지에 입력된 Room 이미지 리스트
     */
    public void saveRoomImages(Room room, List<MultipartFile> roomImageFileList) throws Exception {
        for (int i = 0; i < roomImageFileList.size(); i++) {
            RoomImage createdRoomImage = new RoomImage();
            createdRoomImage.setRoom(room);
            MultipartFile roomImageFile = roomImageFileList.get(i);
            createdRoomImage.setImage(imageService.createImage(room, roomImageFile, i));
            roomImageRepository.save(createdRoomImage);
        }
    }

    public List<RoomImage> findRoomImageByRoom(Room room) {
        List<RoomImage> roomImages = roomImageRepository.findByRoomRoomIdOrderByRoomImageId(room.getRoomId());
        if (roomImages.isEmpty()) {
            throw new IllegalStateException();
        }
        return roomImages;
    }
}
