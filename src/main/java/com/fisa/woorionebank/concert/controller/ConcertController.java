package com.fisa.woorionebank.concert.controller;

import com.fisa.woorionebank.concert.domain.dto.ConcertHistoryDTO;
import com.fisa.woorionebank.concert.domain.dto.RequestDrawDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.domain.dto.responseDto.ResponseDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.ResponseSeatDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class ConcertController {
    private final ConcertService concertService;

    /**
     * 우리 원 더 스테이지 공연 정보 조회
     * */
    @GetMapping("")
    public ResponseEntity<ResponseConcertDTO> searchConcert(@RequestParam Long concertId) {
        return ResponseEntity.ok().body(concertService.searchConcert(concertId));
    }

    /**
     * 우리 원 더 스테이지 응모
     * */
    @PostMapping("/apply")
    public ResponseEntity<ConcertHistoryDTO> applyConcert(@AuthenticationPrincipal Member member, @RequestBody RequestDrawDTO requestDrawDTO) {
        return ResponseEntity.ok().body(concertService.applyConcert(member, requestDrawDTO.getConcertId()));
    }

    /**
     * 우리 원 더 스테이지 응모 당첨
     * */
    @GetMapping("/draw")
    public ResponseEntity<?> drawConcert(@RequestParam Long concertId) {
//        return ResponseEntity.ok().body(concertService.drawConcert(concertId));
        return null;
    }

    /**
     * 우리 원 더 스테이지 당첨 내역 확인
     * */
    @GetMapping("/draw/result")
    public ResponseEntity<?> searchDrawResult(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
            return ResponseEntity.ok().body(concertService.searchDrawResult(member, concertId));
    }

    /**
     * 우리 원 더 스테이지 예매 좌석 조회
     * */
    @GetMapping("/seat")
    public ResponseEntity<?> selectSeat(@RequestParam Long concertId) {
            return ResponseEntity.ok().body(concertService.selectSeat(concertId));
    }

    /**
     * 우리 원 더 스테이지 좌석 예매
     * */
    @PostMapping("/reservation")
    public ResponseEntity<ConcertHistoryDTO> reserveSeat(@AuthenticationPrincipal Member member, @RequestBody RequestSeatDTO requestSeatDTO) {
        return ResponseEntity.ok().body(concertService.reserveSeat(member, requestSeatDTO));
    }
}
