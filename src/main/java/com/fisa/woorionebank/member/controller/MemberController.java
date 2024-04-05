package com.fisa.woorionebank.member.controller;

import com.fisa.woorionebank.member.domain.dto.requestDto.RegisterDTO;
import com.fisa.woorionebank.member.domain.dto.responseDto.ResponseDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.service.MemberService;
import com.fisa.woorionebank.security.TokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            log.info("{요청 확인}");
            Member registeredMember = memberService.createMember(registerDTO);

            // 토큰 생성
            final String token = tokenProvider.create(registeredMember);
            return ResponseEntity.ok().body(token);
        }
        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal Member member) {
        if (member != null) {
            return ResponseEntity.ok().body("Logout Succeed.");
        }
        else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Logout failed.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    @GetMapping("/get")
    public String test() {
        return "안녕~";
    }
}
