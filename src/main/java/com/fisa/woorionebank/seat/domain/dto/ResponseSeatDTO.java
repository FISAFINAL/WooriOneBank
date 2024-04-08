package com.fisa.woorionebank.seat.domain.dto;

import com.fisa.woorionebank.seat.entity.SeatClass;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseSeatDTO {
    private SeatClass seatClass;
    private String seatNumber;
    private int seatX;
    private int seatY;
}
