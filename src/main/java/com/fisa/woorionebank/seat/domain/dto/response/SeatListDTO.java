package com.fisa.woorionebank.seat.domain.dto.response;

import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
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
