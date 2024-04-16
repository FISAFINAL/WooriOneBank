package com.fisa.woorionebank.saving.controller;

import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.*;
import com.fisa.woorionebank.saving.service.SavingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saving")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "최애적금 API", description = "최애적금 관련 기능")
public class SavingController {

    private final SavingService savingService;

    @Operation(summary = "최애적금 생성", description = "회원이 최애적금을 생성합니다.")
    @PostMapping
    public ResponseEntity<SavingDTO> createSaving(@RequestBody SavingCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.createSaving(requestDTO));
    }

    @Operation(summary = "최애적금 조회", description = "회원이 생성한 최애적금 리스트를 조회합니다.")
    @GetMapping("/{memberId}")
    public ResponseEntity<SavingListDTO> findSavings(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(savingService.findSavings(memberId));
    }
    @Operation(summary = "최애적금 조회", description = "회원이 생성한 최애적금 리스트를 조회합니다.")
    @GetMapping()
    public ResponseEntity<SavingListDTO> findSaving(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(savingService.findSaving(member));
    }

    @Operation(summary = "최애적금 규칙 추가", description = "선택한 최애적금에 규칙이름과 규칙 비용을 추가합니다.")
    @PostMapping("/rules")
    public ResponseEntity<SavingRuleDTO> addRule(@RequestBody SavingAddRuleRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.addRule(requestDTO));
    }

    @Operation(summary = "최애적금 규칙 조회", description = "선택한 최애적금의 규칙 리스트를 조회합니다.")
    @GetMapping("/rules/{savingId}")
    public ResponseEntity<RuleListDTO> findRules(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.findRules(savingId));
    }

    @Operation(summary = "최애적금 입금", description = "규칙을 통해 연동된 계좌에서 최애적금으로 입금을 합니다.")
    @PostMapping("/deposit/{ruleId}")
    public ResponseEntity<SavingDTO> deposit(@PathVariable("ruleId") Long ruleId) {
        return ResponseEntity.ok(savingService.ruleDeposit(ruleId));
    }

    @Operation(summary = "최애적금 입금 내역 조회 (X)", description = "최애적금 입금 내역을 조회합니다.")
    @GetMapping("/history/{savingId}")
    public ResponseEntity<SavingHistoryListDTO> findHistory(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.findHistory(savingId));
    }

    @Operation(summary = "최애적금 정보를 상세 조회합니다.", description = "최애적금 기본정보, 입금 내역, 현재금리를 조회합니다.")
    @GetMapping("/info/{savingId}")
    public ResponseEntity<SavingInfoDTO> savingInfo(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.savingInfo(savingId));
    }


}
