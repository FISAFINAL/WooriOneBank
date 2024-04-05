package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.member.entity.Member;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConcertHistoryDTO {
//    private Long concertReservationId;

    private Status status;

//    private Area area;

//    private LocalDateTime ticketingDate;

    private Member member;

//    private Seat seat;

    private Concert concert;
}
