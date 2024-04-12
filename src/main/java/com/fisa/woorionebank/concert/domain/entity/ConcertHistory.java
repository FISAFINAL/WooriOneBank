package com.fisa.woorionebank.concert.domain.entity;

import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.concert.domain.dto.response.ConcertHistoryDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.entity.Seat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
@Table(name = "concert_history")
@Entity
public class ConcertHistory extends BaseEntity {
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

    public void win(Area area) {
        this.status = Status.WIN;
        this.area = area;
    }

    public void reserve(Seat seat) {
        this.status = Status.SUCCESS;
        this.ticketingDate = LocalDateTime.now();
        this.seat = seat;
    }

    public ConcertHistory apply(Status apply, Member member, Concert concert) {
        this.status = apply;
        this.member = member;
        this.concert = concert;

        return this;
    }

    public static ConcertHistory saveConcertHistory(ConcertHistoryDTO concertHistoryDTO){
        ConcertHistory concertHistory = new ConcertHistory();
        concertHistory.status = concertHistoryDTO.getStatus();
        concertHistory.member = concertHistoryDTO.getMember();
        concertHistory.concert = concertHistoryDTO.getConcert();
        return concertHistory;
    }

    public static ConcertHistory of(
        Status status,
        Area area,
        LocalDateTime ticketingDate,
        Member member,
        Seat seat,
        Concert concert
    ) {
        return ConcertHistory.builder()
                .status(status)
                .area(area)
                .ticketingDate(ticketingDate)
                .member(member)
                .seat(seat)
                .concert(concert)
                .build();
    }
}