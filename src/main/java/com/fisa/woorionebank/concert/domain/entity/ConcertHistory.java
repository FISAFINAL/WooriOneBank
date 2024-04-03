package com.fisa.woorionebank.concert.domain.entity;

import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "concert_history")
@Entity
public class ConcertHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "concert_reservation_id")
    private Long concertReservationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Area area;

    private LocalDateTime ticketingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;
}