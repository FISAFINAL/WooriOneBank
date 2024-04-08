package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.domain.entity.*;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
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
    private final SavingRepository savingRepository;

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
        Optional<ConcertHistory> c = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId);

        if (c.isEmpty()) {
            ConcertHistory concertHistory = ConcertHistory.builder()
                    .status(Status.APPLY)
                    .member(member)
                    .concert(concert)
                    .build();

            concertHistoryRepository.save(concertHistory);
        } else {
            log.error("이미 콘서트를 응모한 회원입니다."); // TODO 추후 에러 처리
        }
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

    @Transactional
    public void drawConcert(Member member, Long concertId) {
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

        }

        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId).orElse(null);
        concertHistory.win(Area.R);

        concertHistoryRepository.save(concertHistory);
        // end of R석 당첨 로직

        /* A석 당첨자 뽑기 */
        List<ConcertHistory> concertHistories = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);

        // 만약 apply인 사람들이 없으면 이미 전원 R석 당첨입니다.
        if(!concertHistories.isEmpty()) {
            winners = new HashMap<>();
            winnerPool = new ArrayList<>();
            random = new Random();

            // 랜덤으로 A석 당첨자 선발
            for (int i = 0; i < seatAvailableA && !winnerPool.isEmpty(); i++) {
                concertHistories.get(i).getMember().getMemberId();

                // 적금 가입한 사용자만 선발한다.
                Optional<Long> saving = savingRepository.findByMemberId(1L);
                if(!saving.isEmpty()) {
                    int index = random.nextInt(winnerPool.size());
                    winners.put(winnerPool.remove(index), 0);
                }
            }

            ConcertHistory concertHistoryA = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId).orElse(null);
            concertHistoryA.win(Area.A);

            concertHistoryRepository.save(concertHistory);
        }
        // end of A석 당첨

        /* B석 당첨자 뽑기 */
        List<ConcertHistory> concertHistoriesB = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);

        // 만약 apply인 사람들이 없으면 이미 전원 R석, A석 당첨입니다.
        if(!concertHistoriesB.isEmpty()) {
            winners = new HashMap<>();
            winnerPool = new ArrayList<>();
            random = new Random();

            // 랜덤으로 B석 당첨자 선발
            for (int i = 0; i < seatAvailableB && !winnerPool.isEmpty(); i++) {
                int index = random.nextInt(winnerPool.size());
                winners.put(winnerPool.remove(index), 0);
            }

            ConcertHistory concertHistoryB = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId).orElse(null);
            concertHistoryB.win(Area.B);

            concertHistoryRepository.save(concertHistory);
        }
        // end of B석 당첨
    }

    @Transactional
    public ResponseDrawDTO searchDrawResult(Member member, Long concertId) {
        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new NotFoundException("Concert history not found"));

        ResponseDrawDTO responseDrawDTO = new ResponseDrawDTO();
        responseDrawDTO.setConcertName(concertHistory.getConcert().getConcertName());
        responseDrawDTO.setMemberName(member.getName());
        responseDrawDTO.setArea(concertHistory.getArea());

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

    @Transactional
    public void reserveSeat(Member member, RequestSeatDTO seatDTO) {
        // concert_history의 좌석id(fk), 예매 일시 업데이트
        Seat seat = seatRepository.findSeatIdBySeatXAndSeatY(seatDTO.getSeatX(), seatDTO.getSeatY());
        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), seatDTO.getConcertId()).orElse(null);

        concertHistory.reserve();
        concertHistory.setSeat(seat);

        // TODO 이미 선택된 좌석 처리

        concertHistoryRepository.save(concertHistory);
    }

    public void test() {
        Optional<Long> s = savingRepository.findByMemberId(1L);

        log.info("{}", savingRepository.findByMemberId(1L));


    }
}
