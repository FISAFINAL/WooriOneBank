package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.concert.domain.dto.ConcertHistoryDTO;
import com.fisa.woorionebank.concert.domain.dto.ReserveAvailableDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.domain.entity.*;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.concert.util.ConcertUtils;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.SeatListDTO;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertHistoryRepository concertHistoryRepository;
    private final SeatRepository seatRepository;

    public ResponseConcertDTO searchConcert(Long concertId) {
        final Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        PeriodType period = ConcertUtils.calculatePeriod(concert);

        return ResponseConcertDTO.fromEntity(concert, period);
    }

    @Transactional
    public ConcertHistoryDTO applyConcert(Member member, Long concertId) {
        final Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        final ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId).orElseThrow(
                () -> new CustomException(ErrorCode.ALREADY_APPLIED_Concert)
        );

        ConcertHistory appliedConcert = concertHistory.apply(Status.APPLY, member, concert);
        return ConcertHistoryDTO.of(appliedConcert);
    }

    @Transactional
    public ResponseDrawDTO searchDrawResult(Member member, Long concertId) {
        final ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ConcertHistory));

        return new ResponseDrawDTO(concertHistory.getConcert().getConcertName(), member.getName(), concertHistory.getArea());
    }

    public ReserveAvailableDTO reserveAvailable(Member member, Long concertId) {
        final ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ConcertHistory));

        if(concertHistory.getStatus() == Status.WIN) return new ReserveAvailableDTO(true);
        else throw new CustomException(ErrorCode.INVALID_TICKETING);
    }

    public SeatListDTO selectSeat(Long concertId) {
        final Long concertVenueId = concertRepository.findConcertVenueByConcertId(concertId)
                .orElseThrow(() -> new CustomException((ErrorCode.NOT_FOUND_ConcertVenue)));

        List<Long> seatsIdList = seatRepository.findSeatByConcertVenueId(concertVenueId);
        List<ResponseSeatDTO> seats = new ArrayList<>();

        for (Long seatId : seatsIdList) {
            // 좌석 조회 시 이미 예매가 완료된 좌석(선택 불가), 예매가 가능한 좌석 나누기
            Optional<ConcertHistory> c = concertHistoryRepository.findBySeatIdAndConcertId(seatId, concertId);
            Optional<Seat> seatOptional = seatRepository.findById(seatId);

            seatOptional.ifPresent(seat -> {
                ResponseSeatDTO responseSeatDTO;

                if(c.isEmpty()) {
                    responseSeatDTO = ResponseSeatDTO.fromEntity(seat);
                    responseSeatDTO.setReserved(false);
                }
                else {
                    responseSeatDTO = ResponseSeatDTO.fromEntity(seat);
                    responseSeatDTO.setReserved(true);
                }

                seats.add(responseSeatDTO);
            });
        }

        return new SeatListDTO(seats);
    }

    @Transactional
    public ConcertHistoryDTO reserveSeat(Member member, RequestSeatDTO seatDTO) {
        Seat seat = seatRepository.findSeatIdBySeatXAndSeatY(seatDTO.getSeatX(), seatDTO.getSeatY())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Seat));

        // 이미 선택된 좌석 확인
        concertHistoryRepository.findBySeatIdAndConcertId(seat.getSeatId(), seatDTO.getConcertId())
                .orElseThrow(() -> new CustomException((ErrorCode.ALREADY_RESERVED_SEAT)));

        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), seatDTO.getConcertId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TICKETING));

        concertHistoryRepository.save(ConcertHistory.of(
            Status.SUCCESS,
            concertHistory.getArea(),
            LocalDateTime.now(),
            concertHistory.getMember(),
            seat,
            concertHistory.getConcert()
        ));

        return ConcertHistoryDTO.fromEntity(concertHistory);
    }
}
