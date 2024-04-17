package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertApplyDTO {
    private Status status;

    public static ConcertApplyDTO fromEntity(ConcertHistory concertHistory) {
        return ConcertApplyDTO.builder()
                .status(concertHistory.getStatus())
                .build();
    }
}
