package com.mungnyang.repository.service;

import com.mungnyang.entity.service.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberMemberId(Long memberId);
}
