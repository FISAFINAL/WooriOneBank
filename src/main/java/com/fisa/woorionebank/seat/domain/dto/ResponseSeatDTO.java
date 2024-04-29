package com.fisa.woorionebank.seat.domain.dto;

import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.entity.SeatClass;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResponseSeatDTO {
    private SeatClass seatClass;
    private String seatNumber;
    private int seatX;
    private int seatY;
    private boolean isReserved;

    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    public static ResponseSeatDTO fromEntity(Seat seat) {
        return ResponseSeatDTO.builder()
                .seatClass(seat.getSeatClass())
                .seatNumber(seat.getSeatNumber())
                .seatX(seat.getSeatX())
                .seatY(seat.getSeatY())
                .build();
    }
}
