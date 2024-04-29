package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.Area;
import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import com.fisa.woorionebank.concert.domain.entity.Status;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResponseDrawDTO {
    private String concertName;
    private String memberName;
    private Area area;
    private Status status;
    private PeriodType current; // 현재 시점
}
