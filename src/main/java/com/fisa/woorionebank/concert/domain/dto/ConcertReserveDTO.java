package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ConcertReserveDTO {
    private String concertName;
    private String location;
    private LocalDateTime concertDate;
    private String seatName;

    public static ConcertReserveDTO fromEntity(ConcertHistory concertHistory) {
        return ConcertReserveDTO.builder()
                .concertName(concertHistory.getConcert().getConcertName())
                .location(concertHistory.getConcert().getConcertVenue().getVenueName())
                .concertDate(concertHistory.getConcert().getConcertDate())
                .seatName(concertHistory.getSeat().getSeatNumber())
                .build();
    }
}
