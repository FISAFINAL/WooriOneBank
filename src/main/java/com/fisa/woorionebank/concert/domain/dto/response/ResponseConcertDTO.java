package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ResponseConcertDTO {
    /*
     * 콘서트 제목, 공연 일자, 공연 장소, 라인업, 스페셜 MC, 응모 기간, 당첨, 좌석 예매
     * 현재 시점(지금이 응모 기간인지 체크하기 위함)
     * */
    private String concertName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime checkDate;

    private LocalDateTime ticketingDate;

    private LocalDateTime concertDate;

    private int runningTime;

    private String ageLimit;

    private String imgUrl; // 공연 정보 포스터 이미지

    private String lineup; // 라인업

    private String drawInfo; // 당첨 기준에 관한 정보

    private PeriodType current; // 현재 시점(지금이 응모 기간인지 체크하기 위함)

    public static ResponseConcertDTO fromEntity(Concert concert, PeriodType period) {
        return ResponseConcertDTO.builder()
                .concertName(concert.getConcertName())
                .startDate(concert.getStartDate())
                .endDate(concert.getEndDate())
                .checkDate(concert.getCheckDate())
                .ticketingDate(concert.getTicketingDate())
                .concertDate(concert.getConcertDate())
                .runningTime(concert.getRunningTime())
                .ageLimit(concert.getAgeLimit())
                .imgUrl(concert.getImgUrl())
                .lineup(concert.getLineup())
                .drawInfo(concert.getDrawInfo())
                .current(period)
                .build();
    }

}
