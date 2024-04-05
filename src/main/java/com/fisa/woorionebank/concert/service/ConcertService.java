package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.concert.domain.dto.ConcertHistoryDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.domain.entity.*;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertHistoryRepository concertHistoryRepository;
    private final MemberRepository memberRepository;

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

    public void applyConcert(Member member, Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElse(null);
        ConcertHistory concertHistory = null;

        ConcertHistoryDTO concertHistoryDTO = null;
        concertHistoryDTO.setStatus(Status.NONE);
        concertHistoryDTO.setMember(member);
        concertHistoryDTO.setConcert(concert);

        concertHistory.createConcertHistory(concertHistoryDTO);
        concertHistoryRepository.save(concertHistory);
    }

    public ResponseDrawDTO drawConcert(Member member, Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElse(null);

        ResponseDrawDTO responseDrawDTO = new ResponseDrawDTO();
        responseDrawDTO.setConcertName(concert.getConcertName());
        responseDrawDTO.setMemberName(member.getName());
        responseDrawDTO.setArea(Area.R); // TODO 좌석 당첨 로직을 짜야 합니다.

        // R석 우리카드 실적 높은 사람(1만)
        // A석 적금 가입 고객(3만)
        // B석 신청한 사람 중 랜덤(2만)

        member.getGrade();
        // 일단 신청한 사람 중에 등급별로 나눈다.
        //

        Map<Long, Grade> winners = new HashMap<>();
        Random random = new Random();

        // winner에 값을 넣으려면 일단 당첨된 사람들을 조회해야 한다.
//        memberRepository

//        Member concertHistoryRepository.findByConcertId(concertId); // 이러면 이제 2024 우리 원 더 콘서트 신청한 내역들을 다 가져올 수 있다.

        return responseDrawDTO;
    }
}
