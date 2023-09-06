package com.mungnyang.service.product.accommodation.room;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.ModifyImageDto;
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

    /**
     * 방 이미지 수정하기
     * @param room 해당 방
     * @param imageList 수정 페이지에 입력된 수정 이미지 정보
     * @throws Exception
     */
    public void updateRoomImage(Room room, List<ModifyImageDto> imageList) throws Exception {
        boolean isRepImageDelete = false;
        for (ModifyImageDto modifyImageDto : imageList) {
            if (modifyImageDto.getIsDelete().equals("Y")) {
                Long imageId = modifyImageDto.getImageId();
                RoomImage savedImage = getRoomImageByRoomImageId(imageId);
                if (savedImage.getImage().getIsRepresentative() == IsTrue.YES) {
                    isRepImageDelete = true;
                }
                imageService.deleteImage(room, savedImage.getImage());
                roomImageRepository.delete(savedImage);
                continue;
            }
            if (modifyImageDto.getImageId() == null) {
                RoomImage newImage = new RoomImage();
                newImage.setRoom(room);
                newImage.setImage(imageService.createImage(room, modifyImageDto.getImageFile(), 1));
                roomImageRepository.save(newImage);
            }
        }
        if (isRepImageDelete) {
            List<RoomImage> currentImages = getRoomImageListByRoomId(room.getRoomId());
            currentImages.get(0).getImage().setIsRepresentative(IsTrue.YES);
        }
    }

    /**
     * 모든 방 이미지 삭제
     * @param room 해당 방
     * @throws Exception
     */
    public void deleteAllRoomImages(Room room) throws Exception {
        List<RoomImage> savedRoomImages = getRoomImageListByRoomId(room.getRoomId());
        for (RoomImage savedRoomImage : savedRoomImages) {
            imageService.deleteImage(room, savedRoomImage.getImage());
            roomImageRepository.delete(savedRoomImage);
        }
    }

    /**
     * 숙소 이미지 일련번호로 숙소 이미지 찾기
     * @param roomImageId 숙소 이미지 일련번호
     * @return RoomImage 엔티티
     */
    private RoomImage getRoomImageByRoomImageId(Long roomImageId) {
        return roomImageRepository.findById(roomImageId).orElseThrow(IllegalArgumentException::new);
    }
}
