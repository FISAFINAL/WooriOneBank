package com.fisa.woorionebank.concert.domain.entity;

import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.concert.domain.dto.ConcertDTO;
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
@Table(name = "concert")
@Entity
public class Concert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "concert_id")
    private Long concertId;

    private String concertName; // 공연 제목

    private LocalDateTime startDate; // 공연 응모 시작일

    private LocalDateTime endDate; // 공연 응모 마감일

    private LocalDateTime checkDate; // 공연 당첨 확인일

    private LocalDateTime ticketingDate; // 티켓팅 일자

    private LocalDateTime concertDate; // 공연 일자

    private int runningTime; // 공연 시간

    private String ageLimit; // 관람 연령가

    private String lineup; // 라인업

    private String drawInfo; // 당첨 정보

    private String imgUrl; // 공연 정보 포스터 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_venue_id")
    private ConcertVenue concertVenue;

    public static Concert createConcert(ConcertDTO concertDTO){
        Concert concert = new Concert();
        concert.concertName = concertDTO.getConcertName();
        concert.startDate = concertDTO.getStartDate();
        concert.endDate = concertDTO.getEndDate();
        concert.checkDate = concertDTO.getCheckDate();
        concert.ticketingDate = concertDTO.getTicketingDate();
        concert.concertDate = concertDTO.getConcertDate();
        concert.runningTime = concertDTO.getRunningTime();
        concert.ageLimit = concertDTO.getAgeLimit();
        concert.lineup = concertDTO.getLineup();
        concert.drawInfo = concertDTO.getDrawInfo();
        concert.imgUrl = concertDTO.getImgUrl();
        return concert;
    }
}
