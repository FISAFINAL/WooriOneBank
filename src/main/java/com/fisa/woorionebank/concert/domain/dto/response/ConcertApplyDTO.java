package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertApplyDTO {
    private Status status;

    public static ConcertApplyDTO fromEntity(ConcertHistory concertHistory) {
        return ConcertApplyDTO.builder()
                .status(concertHistory.getStatus())
                .build();
    }
}
