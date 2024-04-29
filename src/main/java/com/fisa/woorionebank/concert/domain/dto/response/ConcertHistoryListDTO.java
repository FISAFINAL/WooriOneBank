package com.fisa.woorionebank.concert.domain.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
public class ConcertHistoryListDTO {
    private List<ConcertHistoryDTO> concertHistoryList = new ArrayList<>();
}
