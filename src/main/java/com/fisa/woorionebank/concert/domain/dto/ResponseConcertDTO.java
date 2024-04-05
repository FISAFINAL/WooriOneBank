package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseConcertDTO {
    /*
     * 콘서트 제목, 공연 일자, 공연 장소, 라인업, 스페셜 MC, 응모 기간, 당첨, 좌석 예매
     * 현재 시점(지금이 응모 기간인지 체크하기 위함)
     * */
    private String concertName;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime checkDate;

    private LocalDateTime ticketingDate;

    private LocalDateTime concertDate;

    private int runningTime;

    private String ageLimit;

    private String lineup; // 라인업

    private String drawInfo; // 당첨 정보

    private PeriodType current; // 현재 시점(지금이 응모 기간인지 체크하기 위함)

}
