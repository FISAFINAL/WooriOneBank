package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.dto.ConcertHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ConcertHistoryListDTO {
    private List<ConcertHistoryDTO> concertHistoryList = new ArrayList<>();
}
