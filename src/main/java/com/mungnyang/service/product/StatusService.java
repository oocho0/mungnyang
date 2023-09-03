package com.mungnyang.service.product;

import com.mungnyang.constant.Booked;
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
     * modelMapper에서 String타입의 status를 Status타입으로 바꿈
     * @param status 바꿀 String타입의 status
     * @return 변환된 Status타입
     */
    public static Status statusConverter(String status){
        return StringUtils.equals(status, Status.OPEN.name()) ? Status.OPEN :
                (StringUtils.equals(status, Status.CLOSED.name()) ? Status.CLOSED : Status.PAUSE);
    }

    /**
     * modelMapper에서 Status타입의 status를 String으로 바꿈
     * @param status 바꿀 Status 타입의 status
     * @return 변환된 String
     */
    public static String statusConverter(Status status) {
        return status == Status.OPEN ? Status.OPEN.name() : (status == Status.CLOSED ? Status.CLOSED.name() : Status.PAUSE.name());
    }

    /**
     * modelMapper에서 String타입의 isTrue를 IsTrue타입으로 바꿈
     * @param isTrue String타입의 isTrue
     * @return 변환된 IsTrue타입
     */
    public static IsTrue isTrueConverter(String isTrue){
        return StringUtils.equals(isTrue, IsTrue.YES.name()) ? IsTrue.YES : IsTrue.NO;
    }

    /**
     * modelMapper에서 String타입의 isAvailable를 IsAvailable타입으로 바꿈
     * @param isAvailable String타입의 isAvailable
     * @return 변환된 IsAvailable타입
     */
    public static Booked isAvailableConverter(String isAvailable) {
        return StringUtils.equals(isAvailable, Booked.AVAILABLE.name()) ? Booked.AVAILABLE :
                (StringUtils.equals(isAvailable, Booked.BOOKED.name()) ? Booked.BOOKED : Booked.UNAVAILABLE);
    }
}
