package com.fisa.woorionebank.concert.domain.dto.request;

import lombok.*;

@Getter
@Builder
@ToString
public class RequestConcertDTO {
    private Long memberId;
    private Long concertId;
}
