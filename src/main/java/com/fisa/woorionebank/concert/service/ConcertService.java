package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.domain.entity.*;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.repository.SeatRepository;
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
    private final SeatRepository seatRepository;

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

        ConcertHistory concertHistory = ConcertHistory.builder()
                .status(Status.NONE)
                .member(member)
                .concert(concert)
                .build();

        concertHistoryRepository.save(concertHistory);
    }

    // 회원 등급을 가산점으로 변환하는 함수
    public int transferPoint(Grade grade) {

        // 회원 등급을 가산점으로 변환하는 함수
        Map<Grade, Integer> map = new HashMap<>();
        Grade[] grades = Grade.values();
        int len = Grade.values().length;

        for (int i = len; i > 0; i--) {
            map.put(grades[i], i);
        }

        return map.get(grade);
    }

    public ResponseDrawDTO drawConcert(Member member, Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElse(null);

        ResponseDrawDTO responseDrawDTO = new ResponseDrawDTO();
        responseDrawDTO.setConcertName(concert.getConcertName());
        responseDrawDTO.setMemberName(member.getName());
        responseDrawDTO.setArea(Area.R); // TODO 좌석 당첨 로직을 짜야 합니다. R석(완료), A석, B석

        /* 좌석 당첨 로직 */
        // R석 우리카드 실적 높은 사람(1만)
        // A석 적금 가입 고객(3만)
        // B석 신청한 사람 중 랜덤(2만)
        List<Member> memberList = concertHistoryRepository.findMemberByConcertId(concertId);

        /*
         * seatAvailable 변수는 가용 좌석 수입니다.
         *  1. Concert entity에 컬럼 추가
         *  2. Seat(공연장 정보) entity 추가하는 방법 대신,
         * 우선 당첨 내역 확인 로직에서만 관리하고 있습니다.
         * */
        int seatAvailableR = 10_000;
        int seatAvailableA = 30_000;
        int seatAvailableB = 20_000;

        /* R석 당첨자 뽑기 */
        // 회원 등급을 가산점으로 변환하기
        Map<Member, Integer> winners = new HashMap<>();
        List<Member> winnerPool = new ArrayList<>();

        for (Member m : memberList) {
            int point = transferPoint(m.getGrade());

            // 우승자 풀에 추가 (가산점만큼 여러 번 추가)
            for (int i = 0; i < point; i++) {
                winnerPool.add(member);
            }
        }

        Random random = new Random();

        // 랜덤으로 R석 당첨자 선발
        for (int i = 0; i < seatAvailableR && !winnerPool.isEmpty(); i++) {
            int index = random.nextInt(winnerPool.size());
            winners.put(winnerPool.remove(index), 0);

            // TODO ConcertHistory > status에 반영해줘야 한다.
        }
        // end of R석 당첨 로직 : winners에 담겨 있음.

        /* TODO A석 당첨자 뽑기 */
        // R석 미당첨자도 A석의 winnersPool에 넣어줘야 한다.

        /* TODO B석 당첨자 뽑기 */


        return responseDrawDTO;
    }

    public List<ResponseSeatDTO> selectSeat(Long concertId) {
        Long concertVenueId = concertRepository.findConcertVenueByConcertId(concertId);
        List<Long> seatsIdList = seatRepository.findSeatByConcertVenueId(concertVenueId);
        List<ResponseSeatDTO> seats = new ArrayList<>();

        for (Long seatId : seatsIdList) {
            Optional<Seat> seatOptional = seatRepository.findById(seatId);
            seatOptional.ifPresent(seat -> {
                ResponseSeatDTO responseSeatDTO = new ResponseSeatDTO();
                responseSeatDTO.setSeatClass(seat.getSeatClass());
                responseSeatDTO.setSeatNumber(seat.getSeatNumber());
                responseSeatDTO.setSeatX(seat.getSeatX());
                responseSeatDTO.setSeatY(seat.getSeatY());
                seats.add(responseSeatDTO);
            });
        }

        return seats;
    }
}
