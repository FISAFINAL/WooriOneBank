package com.fisa.woorionebank.concert.domain.entity;

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
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "concert_id")
    private Long concertId;

    private String concertName; // 공연 제목

    private String location; // 공연 개최 장소

    private LocalDateTime startDate; // 공연 응모 시작일

    private LocalDateTime endDate; // 공연 응모 마감일

    private LocalDateTime checkDate; // 공연 당첨 확인일

    private LocalDateTime ticketingDate; // 티켓팅 일자

    private LocalDateTime concertDate; // 공연 일자

    private int runningTime; // 공연 시간

    private String ageLimit; // 관람 연령가

    private String lineup; // 라인업

    private String drawInfo; // 당첨 정보
}
