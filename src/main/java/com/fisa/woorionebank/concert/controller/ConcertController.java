package com.fisa.woorionebank.concert.controller;

import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.domain.dto.responseDto.ResponseDTO;
import com.fisa.woorionebank.member.entity.Member;
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
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class ConcertController {
    private final ConcertService concertService;

    /**
     * 우리 원 더 스테이지 공연 정보 조회
     * */
    @GetMapping("")
    public ResponseEntity<?> searchConcert(@RequestParam Long concertId) {
        try {
            ResponseConcertDTO concertDTO = concertService.searchConcert(concertId);
            return ResponseEntity.ok().body(concertDTO);
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * 우리 원 더 스테이지 응모
     * */
    @PostMapping("/apply")
    public ResponseEntity<?> applyConcert(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        try {
            concertService.applyConcert(member, concertId);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * 우리 원 더 스테이지 당첨 내역 확인
     * */
    @GetMapping("/draw")
    public ResponseEntity<?> drawConcert(@AuthenticationPrincipal Member member, @RequestParam Long concertId) {
        try {
            ResponseDrawDTO responseDrawDTO = concertService.drawConcert(member, concertId);
            return ResponseEntity.ok().body(responseDrawDTO);
        } catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }
}
