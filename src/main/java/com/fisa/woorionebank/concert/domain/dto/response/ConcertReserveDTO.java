package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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
