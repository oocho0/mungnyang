package com.mungnyang.service.product;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class StatusService {
    /**
     * String타입의 status를 Status타입으로 바꿈
     * @param status 바꿀 String타입의 status
     * @return 변환된 Status타입
     */
    public static Status statusConverter(String status){
        return StringUtils.equals(status, Status.OPEN.name()) ? Status.OPEN :
                (StringUtils.equals(status, Status.CLOSED.name()) ? Status.CLOSED : Status.PAUSE);
    }

    /**
     * Status타입의 status를 String으로 바꿈
     * @param status 바꿀 Status 타입의 status
     * @return 변환된 String
     */
    public static String statusConverter(Status status) {
        return status == Status.OPEN ? Status.OPEN.name() : (status == Status.CLOSED ? Status.CLOSED.name() : Status.PAUSE.name());
    }



    /**
     * ReservationStatus타입의 reservationStatus를 String타입으로 바꿈
     * @param reservationStatus ReservationStatus타입의 reservationStatus
     * @return 변환된 String
     */
    public static String reservationStatusConverter(ReservationStatus reservationStatus) {
        return reservationStatus == ReservationStatus.RESERVATION ? ReservationStatus.RESERVATION.name() : ReservationStatus.CANCEL.name();
    }
}
