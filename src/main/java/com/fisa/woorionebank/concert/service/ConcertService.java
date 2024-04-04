package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import com.fisa.woorionebank.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public ResponseConcertDTO searchConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElse(null);

        /*
        * 공연 이벤트 기간 계산 로직
        * startDate, endDate, checkDate, ticketingDate, concertDate를 기준으로 각 단계별로 현재 시점이 어느 기간에 해당하는지 계산합니다.
        * 결과값은 PeriodType으로 반환됩니다.
        * */
        LocalDateTime startDate = concert.getStartDate();
        LocalDateTime endDate = concert.getEndDate();
        LocalDateTime checkDate = concert.getCheckDate();
        LocalDateTime ticketingDate = concert.getTicketingDate();
        LocalDateTime concertDate = concert.getConcertDate();

        LocalDateTime now = LocalDateTime.now();

        PeriodType period;

        if (now.isAfter(startDate) && now.isBefore(endDate)) {
            period = PeriodType.ENTRY_PERIOD;
        } else if (now.isAfter(checkDate) && now.isBefore(ticketingDate)) {
            period = PeriodType.CONFIRMATION_PERIOD;
        } else if (now.isAfter(ticketingDate) && now.isBefore(ticketingDate.plusDays(1).minusSeconds(1))) {
            period = PeriodType.TICKETING_PERIOD;
        } else if (now.isAfter(ticketingDate.plusDays(1)) && now.isBefore(concertDate)) {
            period = PeriodType.CONCERT_PERIOD;
        } else {
            period = PeriodType.NONE; // 공연 이벤트 기간이 아님.
        }

        ResponseConcertDTO concertDTO = new ResponseConcertDTO();
        concertDTO.setConcertName(concert.getConcertName());
        concertDTO.setLocation(concert.getLocation());
        concertDTO.setStartDate(startDate);
        concertDTO.setEndDate(endDate);
        concertDTO.setCheckDate(checkDate);
        concertDTO.setTicketingDate(ticketingDate);
        concertDTO.setConcertDate(concertDate);
        concertDTO.setRunningTime(concert.getRunningTime());
        concertDTO.setAgeLimit(concert.getAgeLimit());
        concertDTO.setLineup(concert.getLineup());
        concertDTO.setDrawInfo(concert.getDrawInfo());
        concertDTO.setCurrent(period);

        return concertDTO;
    }

}
