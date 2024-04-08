package com.fisa.woorionebank.seat.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RequestSeatDTO {
    private Long concertId;
    private int seatX;
    private int seatY;
}
