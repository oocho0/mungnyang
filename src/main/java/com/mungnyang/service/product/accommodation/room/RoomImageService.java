package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.accommodation.room.ModifyRoomDto;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.product.accommodation.room.RoomImage;
import com.mungnyang.repository.product.accommodation.room.RoomImageRepository;
import com.mungnyang.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    /**
     * RoomId로 RoomImage 리스트 찾기
     * @param roomId 해당 방의 일련번호
     * @return 해당 방의 RoomImage 에티티 리스트
     */
    public List<RoomImage> getRoomImageListByRoomId(Long roomId) {
        List<RoomImage> roomImages = roomImageRepository.findByRoomRoomIdOrderByRoomImageId(roomId);
        if (roomImages.isEmpty()) {
            throw new IllegalStateException();
        }
        return roomImages;
    }

    /**
     * RoomId로 해당 방 수정 화면에 나타낼 이미지 정보 리스트 찾기
     * @param roomId 해당 방의 일련번호
     * @return 해당 방의 ModifyImageDto List
     */
    public List<ModifyImageDto> getModifyImageDtoListByRoomId(Long roomId) {
        List<RoomImage> roomImageList = getRoomImageListByRoomId(roomId);
        List<ModifyImageDto> modifyImageDtoList = new ArrayList<>();
        for (RoomImage roomImage : roomImageList) {
            modifyImageDtoList.add(ModifyImageDto.builder()
                            .imageId(roomImage.getRoomImageId())
                            .imageFileName(roomImage.getImage().getFileName())
                    .build());
        }
        return modifyImageDtoList;
    }
}
