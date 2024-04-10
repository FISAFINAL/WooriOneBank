package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.member.entity.Member;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ConcertHistoryDTO {
    private Status status;
    private Member member;
    private Concert concert;

//    public static ResponseConcertDTO fromEntity(Concert concert, PeriodType period) {
//        return ResponseConcertDTO.builder()
//                .concertName(concert.getConcertName())
//                .startDate(concert.getStartDate())
//                .endDate(concert.getEndDate())
//                .checkDate(concert.getCheckDate())
//                .ticketingDate(concert.getTicketingDate())
//                .concertDate(concert.getConcertDate())
//                .runningTime(concert.getRunningTime())
//                .ageLimit(concert.getAgeLimit())
//                .lineup(concert.getLineup())
//                .drawInfo(concert.getDrawInfo())
//                .current(period)
//                .build();
//    }
    public static ConcertHistoryDTO fromEntity(ConcertHistory concertHistory) {
        return ConcertHistoryDTO.builder()
                .status(concertHistory.getStatus())
                .member(concertHistory.getMember())
                .concert(concertHistory.getConcert())
                .build();
    }

    public static ConcertHistoryDTO of(ConcertHistory concertHistory) {
        return new ConcertHistoryDTO(
            concertHistory.getStatus(),
            concertHistory.getMember(),
            concertHistory.getConcert()
        );
    }
}
