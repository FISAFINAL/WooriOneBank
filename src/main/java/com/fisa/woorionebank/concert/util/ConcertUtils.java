package com.fisa.woorionebank.concert.util;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import com.fisa.woorionebank.member.entity.Grade;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConcertUtils {
    private ConcertUtils() {
        throw new IllegalStateException("ConcertUtils 인스턴스는 생성할 수 없습니다.");
    }

    /*
     * 공연 이벤트 기간 계산 로직
     * startDate, endDate, checkDate, ticketingDate, concertDate를 기준으로 각 단계별로 현재 시점이 어느 기간에 해당하는지 계산합니다.
     * 결과값은 PeriodType으로 반환됩니다.
     * */
    public static PeriodType calculatePeriod(Concert concert) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = concert.getStartDate();
        LocalDateTime endDate = concert.getEndDate();
        LocalDateTime checkDate = concert.getCheckDate();
        LocalDateTime ticketingDate = concert.getTicketingDate();
        LocalDateTime concertDate = concert.getConcertDate();

        if (now.isAfter(startDate) && now.isBefore(endDate)) {
            return PeriodType.ENTRY_PERIOD;
        } else if (now.isAfter(checkDate) && now.isBefore(ticketingDate)) {
            return PeriodType.CONFIRMATION_PERIOD;
        } else if (now.isAfter(ticketingDate) && now.isBefore(ticketingDate.plusDays(1).minusSeconds(1))) {
            return PeriodType.TICKETING_PERIOD;
        } else if (now.isAfter(ticketingDate.plusDays(1)) && now.isBefore(concertDate)) {
            return PeriodType.CONCERT_PERIOD;
        } else {
            return PeriodType.NONE;
        }
    }
}
