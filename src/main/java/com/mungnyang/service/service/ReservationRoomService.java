package com.mungnyang.service.service;

import com.mungnyang.dto.product.accommodation.room.InitializeReservationRoomDto;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.ReservationRoom;
import com.mungnyang.repository.service.ReservationRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationRoomService {

    private final ReservationRoomRepository reservationRoomRepository;

    public void saveReservationRoom(Room room, List<InitializeReservationRoomDto> initializeReservationRoomDtoList){
        for (InitializeReservationRoomDto initializeReservationRoomDto : initializeReservationRoomDtoList) {
            ReservationRoom createdReservationRoom = ReservationRoom.builder()
                    .BookingRoomId(null)
                    .room(room)
                    .reservation(null)
                    .reservationPrice(null)
                    .checkInDate(initializeReservationRoomDto.getCheckInDate())
                    .checkOutDate(initializeReservationRoomDto.getCheckOutDate())
                    .build();
            reservationRoomRepository.save(createdReservationRoom);
        }
    }

}
