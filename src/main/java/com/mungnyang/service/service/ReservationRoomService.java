package com.mungnyang.service.service;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.dto.service.*;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.Reservation;
import com.mungnyang.entity.service.ReservationRoom;
import com.mungnyang.repository.service.ReservationRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationRoomService {
    private final ReservationRoomRepository reservationRoomRepository;

    /**
     * 숙소 관리자가 숙소 등록 페이지에 입력한 예약 현황 정보인 reservation-room 객체 저장
     *
     * @param room                             예약된 방
     * @param initializeReservationRoomDtoList 체크인 체크아웃 정보만 담긴 DTO
     */
    public void saveReservationRoomForInitialize(Room room, List<InitializeReservationRoomDto> initializeReservationRoomDtoList) {
        for (InitializeReservationRoomDto initializeReservationRoomDto : initializeReservationRoomDtoList) {
            ReservationRoom createdReservationRoom = ReservationRoom.builder()
                    .room(room)
                    .checkInDate(initializeReservationRoomDto.getCheckInDate())
                    .checkOutDate(initializeReservationRoomDto.getCheckOutDate())
                    .reservationStatus(ReservationStatus.RESERVATION)
                    .build();
            reservationRoomRepository.save(createdReservationRoom);
        }
    }

    /**
     * 신규 예약-방 저장하기
     *
     * @param room                해당 방 엔티티
     * @param reservation         해당 예약 엔티티
     * @param reservationRoomList 예약-방 정보 리스트
     */
    public List<Long> saveReservationRoom(Room room, Reservation reservation, List<CreateReservationRoomDto> reservationRoomList) {
        List<Long> cartRoomIdList = new ArrayList<>();
        for (CreateReservationRoomDto createReservationRoomDto : reservationRoomList) {
            reservationRoomRepository.save(ReservationRoom.builder()
                    .room(room)
                    .reservation(reservation)
                    .headCount(createReservationRoomDto.getHeadCount())
                    .checkInDate(createReservationRoomDto.getCheckInDate())
                    .checkOutDate(createReservationRoomDto.getCheckOutDate())
                    .reservationStatus(ReservationStatus.RESERVATION)
                    .build());
            if (createReservationRoomDto.getCartRoomId() != null) {
                cartRoomIdList.add(createReservationRoomDto.getCartRoomId());
            }
        }
        return cartRoomIdList;
    }

    public void deleteReservationRoom(Long reservationId) {
        List<ReservationRoom> reservationRoomList = getReservationRoomByReservationReservationId(reservationId);
        for (ReservationRoom reservationRoom : reservationRoomList) {
            reservationRoom.setReservationStatus(ReservationStatus.CANCEL);
        }
    }

    /**
     * 숙소 페이지의 예약 가능한 날에 표시될 방 예약 현황 정보
     *
     * @param roomId 해당 방 일련번호
     * @return 방 예약 현황 정보가 들어있는 CalendarShowReservationRoomDto
     */
    public List<CalendarShowReservationRoomDto> getReservationRoomDtoListByRoomIdForDetailPage(Long roomId) {
        List<CalendarShowReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
        List<ReservationRoom> reservationRoomList = getReservationRoomListByRoomId(roomId, LocalDateTime.now());
        for (ReservationRoom reservationRoom : reservationRoomList) {
            reservationRoomDtoList.add(CalendarShowReservationRoomDto.builder()
                    .id(reservationRoom.getReservationRoomId())
                    .title("예약 불가")
                    .start(reservationRoom.getCheckInDate())
                    .end(reservationRoom.getCheckOutDate())
                    .build());
        }
        return reservationRoomDtoList;
    }

    /**
     * RoomId로 해당 방의 수정 화면에 표시될 reservation-room 정보 리스트 찾기
     *
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoomDto 리스트
     */
    public List<CalendarShowReservationRoomDto> getReservationRoomDtoListByRoomId(Long roomId, IsTrue isInitialRecord) {
        List<CalendarShowReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
        List<ReservationRoom> reservationRoomList = new ArrayList<>();
        if (isInitialRecord == IsTrue.YES) {
            reservationRoomList = reservationRoomRepository.findByRoomRoomIdAndReservationNullAndReservationStatus(roomId, ReservationStatus.RESERVATION);
            if (reservationRoomList.isEmpty()) {
                return null;
            }
            for (ReservationRoom reservationRoom : reservationRoomList) {
                reservationRoomDtoList.add(CalendarShowReservationRoomDto.builder()
                        .id(reservationRoom.getReservationRoomId())
                        .title("예약됨")
                        .start(reservationRoom.getCheckInDate())
                        .end(reservationRoom.getCheckOutDate())
                        .extendedProps(CalendarShowReservationRoomDto.ExtendedProps.builder()
                                .roomId(reservationRoom.getRoom().getRoomId())
                                .build())
                        .build());
            }
        } else {
            reservationRoomList = reservationRoomRepository.findByRoomRoomIdAndReservationNotNullAndReservationStatus(roomId, ReservationStatus.RESERVATION);
            if (reservationRoomList.isEmpty()) {
                return null;
            }
            for (ReservationRoom reservationRoom : reservationRoomList) {
                Integer days = (int) ChronoUnit.DAYS.between(reservationRoom.getCheckInDate(), reservationRoom.getCheckOutDate()) + 1;
                reservationRoomDtoList.add(CalendarShowReservationRoomDto.builder()
                        .id(reservationRoom.getReservationRoomId())
                        .title(reservationRoom.getReservation().getMember().getName())
                        .start(reservationRoom.getCheckInDate())
                        .end(reservationRoom.getCheckOutDate())
                        .extendedProps(CalendarShowReservationRoomDto.ExtendedProps.builder()
                                .roomId(reservationRoom.getRoom().getRoomId())
                                .reservationId(reservationRoom.getReservation().getReservationId())
                                .totalPrice((int) (days * reservationRoom.getRoom().getRoomPrice()))
                                .build())
                        .build());
            }
        }
        return reservationRoomDtoList;
    }

    /**
     * 방 예약 정보 수정하기
     *
     * @param room            해당 방 일련번호
     * @param reservationList 화면에 표시된 예약 정보 리스트
     */
    public void updateReservationRoom(Room room, List<ModifyReservationRoomDto> reservationList) {
        for (ModifyReservationRoomDto modifyReservationRoomDto : reservationList) {
            if (modifyReservationRoomDto.getIsDelete().equals("Y")) {
                Long reservationRoomId = modifyReservationRoomDto.getReservationRoomId();
                ReservationRoom savedReservationRoom = getReservationRoomByReservationRoomId(reservationRoomId);
                reservationRoomRepository.delete(savedReservationRoom);
                continue;
            }
            if (modifyReservationRoomDto.getReservationRoomId() == null) {
                ReservationRoom newReservationRoom = ReservationRoom.builder()
                        .room(room)
                        .checkInDate(modifyReservationRoomDto.getCheckInDate())
                        .checkOutDate(modifyReservationRoomDto.getCheckOutDate())
                        .build();
                reservationRoomRepository.save(newReservationRoom);
            }
        }
    }

    /**
     * 예약 화면에 나타날 예약 방 정보 리스트 반환
     *
     * @param reservationId 해당 예약 일련번호
     * @return 예약 방 정보 ReservationRoomDto 리스트
     */
    public List<ReservationRoomDto> getReservationRoomList(Long reservationId) {
        List<ReservationRoom> reservationRoomList = getReservationRoomByReservationReservationId(reservationId);
        List<ReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
        for (ReservationRoom reservationRoom : reservationRoomList) {
            Integer days = (int) ChronoUnit.DAYS.between(reservationRoom.getCheckInDate(), reservationRoom.getCheckOutDate()) + 1;
            reservationRoomDtoList.add(ReservationRoomDto.builder()
                    .reservationRoomId(reservationRoom.getReservationRoomId())
                    .accommodationId(reservationRoom.getRoom().getAccommodation().getAccommodationId())
                    .accommodationName(reservationRoom.getRoom().getAccommodation().getAccommodationName())
                    .roomId(reservationRoom.getRoom().getRoomId())
                    .roomName(reservationRoom.getRoom().getRoomName())
                    .roomPrice(reservationRoom.getRoom().getRoomPrice())
                    .headCount(reservationRoom.getHeadCount())
                    .days(days)
                    .checkInDate(reservationRoom.getCheckInDate())
                    .checkOutDate(reservationRoom.getCheckOutDate())
                    .build());
        }
        return reservationRoomDtoList;
    }

    private List<ReservationRoom> getReservationRoomByReservationReservationId(Long reservationId) {
        return reservationRoomRepository.findByReservationReservationId(reservationId);
    }

    /**
     * 예약 바구니에 들어있는 장바구니-방이 이미 예약되었는지 확인해서 반환
     *
     * @param roomId       해당 방 일련번호
     * @param checkInDate  장바구니-방 체크인날
     * @param checkOutDate 장바구니-방 체크아웃날
     * @return 예약되어있으면 YES, 아니면 NO
     */
    public String getIsAlreadyBooked(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        Long count = reservationRoomRepository.findReservationRoomForCartRoom(roomId, checkInDate, checkOutDate);
        if (count > 0) {
            return "YES";
        }
        return "No";
    }

    /**
     * RoomId로 reservation-room 엔티티 리스트 찾기
     *
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoom 엔티티 리스트
     */
    private List<ReservationRoom> getReservationRoomListByRoomId(Long roomId, LocalDateTime today) {
        return reservationRoomRepository.findByRoomRoomIdAndCheckOutDateAfterAndReservationStatusOrderByReservationRoomId(roomId, today, ReservationStatus.RESERVATION);
    }

    /**
     * ReservationRoomId로 ReservationRoom 반환
     *
     * @param reservationRoomId 해당 예약-방 일련번호
     * @return ReservationRoom 엔티티
     */
    private ReservationRoom getReservationRoomByReservationRoomId(Long reservationRoomId) {
        return reservationRoomRepository.findById(reservationRoomId).orElseThrow(IllegalArgumentException::new);
    }

}
