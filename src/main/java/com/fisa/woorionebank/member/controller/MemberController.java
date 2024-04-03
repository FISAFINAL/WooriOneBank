package com.fisa.woorionebank.member.controller;

import com.fisa.woorionebank.config.Header;
import com.fisa.woorionebank.member.domain.dto.MemberDTO;
import com.fisa.woorionebank.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/user")
    @Parameter(name = "gettest", description = "getAPI")
    @Operation(summary = "유저 저장", description = "유저 정보를 DTO로 전송하고 DTO로 반환합니다.")
    public Header<MemberDTO> createMember(@RequestBody MemberDTO memberDTO){
        log.info("{요청 확인}");
        return Header.description(memberService.createMember(memberDTO), "유저 저장 완료");
    }
}
