package com.fisa.woorionebank.seat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatListDTO {
    private List<ResponseSeatDTO> responseSeatDTOList = new ArrayList<>();
}
