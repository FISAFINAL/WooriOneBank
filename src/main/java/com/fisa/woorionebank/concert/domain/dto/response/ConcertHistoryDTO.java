package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.member.entity.Member;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ConcertHistoryDTO {
    private Status status;
    private Member member;
    private Concert concert;

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
