package com.fisa.woorionebank.member.controller;

import com.fisa.woorionebank.member.domain.dto.request.LoginDto;
import com.fisa.woorionebank.member.domain.dto.request.RegisterDTO;
import com.fisa.woorionebank.member.domain.dto.response.MemberDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.service.MemberService;
import com.fisa.woorionebank.security.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "회원 API", description = "회원과 관련된 REST API")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/member/signup")
    @Operation(summary = "회원 가입", description = "가입이 완료되면 JWT 토큰을 발급합니다.")
    public ResponseEntity<MemberDTO> registerMember(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(memberService.createMember(registerDTO));
    }

    @PostMapping("/member/login")
    public ResponseEntity<MemberDTO> loginMember(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok().body(memberService.login(loginDto));
    }

    // jwt 테스트용 메소드입니다.
    @GetMapping("/member/get")
    public String loginMember(@AuthenticationPrincipal Member member , @RequestBody LoginDto loginDto) {
        return member.getName() + " " + loginDto.getId();
    }
}
