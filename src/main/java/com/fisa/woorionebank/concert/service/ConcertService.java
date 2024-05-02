package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.concert.domain.dto.response.*;
import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.PeriodType;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.concert.util.ConcertUtils;
//import com.fisa.woorionebank.config.redisson.DistributedLock;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.response.SeatListDTO;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.repository.SeatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ConcertHistoryRepository concertHistoryRepository;
    private final SeatRepository seatRepository;

    public ConcertService(ConcertRepository concertRepository, ConcertHistoryRepository concertHistoryRepository, SeatRepository seatRepository) {
        this.concertRepository = concertRepository;
        this.concertHistoryRepository = concertHistoryRepository;
        this.seatRepository = seatRepository;
    }

    public ResponseConcertDTO searchConcert(Long concertId) {
        final Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        PeriodType period = ConcertUtils.calculatePeriod(concert);


        ResponseConcertDTO responseConcertDTO = ResponseConcertDTO.fromEntity(concert, period);

        log.info("공연 조회");

        return responseConcertDTO;
    }

    @Transactional
    public ConcertApplyDTO applyConcert(Member member, Long concertId) {
        final Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        final Optional<ConcertHistory> concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId);

        ConcertHistory appliedConcert;

        if(concertHistory.isEmpty()) {
            appliedConcert = concertHistoryRepository.save(ConcertHistory.saveConcertHistory(new ConcertHistoryDTO(
                    Status.APPLY,
                    member,
                    concert
            )));

        } else throw new CustomException(ErrorCode.ALREADY_APPLIED_CONCERT);

        return ConcertApplyDTO.fromEntity(appliedConcert);
    }

    public ResponseDrawDTO searchDrawResult(Member member, Long concertId) {
        final ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ConcertHistory));

        PeriodType period = ConcertUtils.calculatePeriod(concertHistory.getConcert());

        return new ResponseDrawDTO(concertHistory.getConcert().getConcertName(), member.getName(), concertHistory.getArea(), concertHistory.getStatus(), period);
    }

    public ReserveAvailableDTO reserveAvailable(Member member, Long concertId) {
        final ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ConcertHistory));

        if(concertHistory.getStatus() == Status.WIN) return new ReserveAvailableDTO(true);
        else if(concertHistory.getStatus() == Status.SUCCESS) throw new CustomException(ErrorCode.ALREADY_BOOKED_CONCERT);
        else throw new CustomException(ErrorCode.NOT_WIN_Member);
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

                if (c.isEmpty()) {
                    responseSeatDTO = ResponseSeatDTO.fromEntity(seat);
                    responseSeatDTO.setReserved(false);
                } else {
                    responseSeatDTO = ResponseSeatDTO.fromEntity(seat);
                    responseSeatDTO.setReserved(true);
                }

                seats.add(responseSeatDTO);
            });
        }

        return new SeatListDTO(seats);
    }

    @Transactional
    public ConcertReserveDTO reserveSeat(Member member, RequestSeatDTO seatDTO) {
        Seat seat = seatRepository.findSeatIdBySeatXAndSeatY(seatDTO.getSeatX(), seatDTO.getSeatY())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Seat));

        // 이미 선택된 좌석 확인 , 선택되지 않았으면 seat가 null
        Optional<ConcertHistory> c = concertHistoryRepository.findBySeatIdAndConcertId(seat.getSeatId(), seatDTO.getConcertId());

        if(!c.isPresent()) {
            ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), seatDTO.getConcertId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TICKETING));

            concertHistory.reserve(seat);
            concertHistoryRepository.save(concertHistory);

            return ConcertReserveDTO.fromEntity(concertHistory);
        } else throw new CustomException(ErrorCode.ALREADY_RESERVED_SEAT);
    }

    /**
     * 분산락 티켓팅
     */
//    @DistributedLock(key = "#lockName")
//    public ConcertReserveDTO reserveSeat2(String lockName, Member member, RequestSeatDTO seatDTO) {
//        Seat seat = seatRepository.findSeatIdBySeatXAndSeatY(seatDTO.getSeatX(), seatDTO.getSeatY())
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Seat));
//
//        // 이미 선택된 좌석 확인 , 선택되지 않았으면 seat가 null
//        Optional<ConcertHistory> c = concertHistoryRepository.findBySeatIdAndConcertId(seat.getSeatId(), seatDTO.getConcertId());
//
//        if (!c.isPresent()) {
//            ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), seatDTO.getConcertId())
//                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TICKETING));
//
//            concertHistory.reserve(seat);
//            concertHistoryRepository.save(concertHistory);
//
//            return ConcertReserveDTO.fromEntity(concertHistory);
//        }
//
//        throw new CustomException(ErrorCode.ALREADY_RESERVED_SEAT);
//    }

    public ConcertReserveDTO searchReserve(Member member, Long concertId) {
        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), concertId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ConcertHistory));

        Status status = concertHistory.getStatus();

        if(status.equals(Status.APPLY)) {
            throw new CustomException(ErrorCode.NOT_WIN_Member); // 응모 당첨 내역이 없음.
        } else if(status.equals(Status.WIN)) {
            throw new CustomException(ErrorCode.ON_SITE_TICKETING); // 당첨됐지만 좌석 예매를 하지 않음. -> 티켓 현장 수령 대상
        } else if(status.equals(Status.SUCCESS)) {
            return ConcertReserveDTO.fromEntity(concertHistory); // Status.SUCCESS -> 좌석 예매 정보 반환
        } else {
            throw new CustomException(ErrorCode.INVALID_TICKETING); // 공연을 응모하지 않은 회원입니다.
        }
    }
}
