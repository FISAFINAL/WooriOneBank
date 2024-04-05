package com.fisa.woorionebank.concert.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RequestConcertDTO {
    private Long memberId;
    private Long concertId;
}
