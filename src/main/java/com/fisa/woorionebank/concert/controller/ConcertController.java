package com.fisa.woorionebank.concert.controller;

import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.domain.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }
}
