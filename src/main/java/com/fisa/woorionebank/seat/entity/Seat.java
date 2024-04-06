package com.fisa.woorionebank.seat.entity;

import com.fisa.woorionebank.concert.domain.entity.ConcertVenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "seat")
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private Long seatId;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;

    private String seatNumber;

    private int seatX;

    private int seatY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_venue_id")
    private ConcertVenue concertVenue;
}
