package com.mungnyang.entity.service;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation extends WriterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime reservationDate;
    private String reservationStatus;
}
