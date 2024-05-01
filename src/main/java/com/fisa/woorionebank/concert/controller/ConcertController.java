package com.fisa.woorionebank.concert.controller;

import com.fisa.woorionebank.concert.domain.dto.request.RequestDrawDTO;
import com.fisa.woorionebank.concert.domain.dto.response.*;
import com.fisa.woorionebank.concert.service.ConcertDrawService;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.response.SeatListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "공연 API", description = "공연 응모, 예매 관련 기능")
public class ConcertController {
    private final ConcertService concertService;
    private final ConcertDrawService concertDrawService;

    /**
     * 우리 원 더 스테이지 공연 정보 조회
     * */
    @Operation(summary = "공연 정보 조회", description = "3/1 ~ 4/30(공연 응모 기간) 공연 상세 정보를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<ResponseConcertDTO> searchConcert(@RequestParam Long concertId) {
        log.info("공연 정보 조회 호출");
        return ResponseEntity.ok().body(concertService.searchConcert(concertId));
    }

    /**
     * 우리 원 더 스테이지 응모
     * */
    @Operation(summary = "공연 응모", description = "3/1 ~ 4/30(공연 응모 기간) 공연 상세 정보 화면에서 공연을 응모합니다.")
    @GetMapping("/apply")
    public ResponseEntity<ConcertApplyDTO> applyConcert(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        log.info("공연 응모 호출");
        return ResponseEntity.ok().body(concertService.applyConcert(member, concertId));
    }

    /**
     * 우리 원 더 스테이지 응모 당첨
     * */
    @Operation(summary = "공연 당첨자 선발", description = "5/1 0시 ~ 5/1 18시 이전에 공연 당첨자를 선발합니다.")
    @GetMapping("/draw/winner")
    public ResponseEntity<WinnersCountDTO> drawConcert(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        log.info("공연 당첨자 선발 호출");
        return ResponseEntity.ok().body(concertDrawService.drawConcert(concertId));
    }

    /**
     * 우리 원 더 스테이지 당첨 내역 확인
     * */
    @Operation(summary = "공연 당첨 내역 확인", description = "5/1 18시 이후(당첨 확인 기간) 공연 당첨 결과를 조회합니다.")
    @GetMapping("/draw/result")
    public ResponseEntity<ResponseDrawDTO> searchDrawResult(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        log.info("공연 당첨 내역 확인 호출");
        return ResponseEntity.ok().body(concertService.searchDrawResult(member, concertId));
    }

    /**
     * 우리 원 더 스테이지 공연 예매 가능한 회원인지 조회
     * : 당첨되지 않은 고객 접근 불가
     * */
    @Operation(summary = "공연 예매 가능한 회원인지 조회", description = "5/2 19시 이후(공연 예매 기간) 공연에 당첨된 회원만 접근 가능합니다.")
    @GetMapping("/seat/auth")
    public ResponseEntity<ReserveAvailableDTO> reserveAvailable(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        log.info("공연 예매가능 여부 조회 호출");
        return ResponseEntity.ok().body(concertService.reserveAvailable(member, concertId));
    }

    /**
     * 우리 원 더 스테이지 예매 좌석 조회
     * */
    @Operation(summary = "공연 예매 > 좌석 조회", description = "5/2 19시 이후(공연 예매 기간) 좌석을 조회합니다.")
    @GetMapping("/seat")
    public ResponseEntity<SeatListDTO> selectSeat(@RequestParam Long concertId) {
        log.info("좌석 조회 호출");
            return ResponseEntity.ok().body(concertService.selectSeat(concertId));
    }

    /**
     * 우리 원 더 스테이지 좌석 예매
     * */
    @Operation(summary = "좌석 예매", description = "5/2 19시 이후(공연 예매 기간) 좌석을 예매합니다.")
    @PostMapping("/reservation")
    public ResponseEntity<ConcertReserveDTO> reserveSeat(@AuthenticationPrincipal Member member, @RequestBody RequestSeatDTO requestSeatDTO) {
        log.info("좌석 예매 호출");
        return ResponseEntity.ok().body(concertService.reserveSeat(member, requestSeatDTO));
    }

//    /**
//     * 좌석 예매 (분산락)
//     * */
//    @Operation(summary = "좌석 예매", description = "5/2 19시 이후(공연 예매 기간) 좌석을 예매합니다.")
//    @PostMapping("/reservation")
//    public ResponseEntity<ConcertReserveDTO> reserveSeat2(@AuthenticationPrincipal Member member, @RequestBody RequestSeatDTO requestSeatDTO) {
//        return ResponseEntity.ok().body(concertService.reserveSeat2(member, requestSeatDTO));
//    }

    /**
     * 우리 원 더 스테이지 좌석 예매 결과 조회
     * */
    @Operation(summary = "좌석 예매 결과 조회", description = "5/2 19시 이후(공연 예매 기간) 좌석 예매 결과를 조회합니다.")
    @GetMapping("/reservation/info")
    public ResponseEntity<ConcertReserveDTO> searchReserve(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        log.info("예매 결과 호출");
        return ResponseEntity.ok().body(concertService.searchReserve(member, concertId));
    }
}
