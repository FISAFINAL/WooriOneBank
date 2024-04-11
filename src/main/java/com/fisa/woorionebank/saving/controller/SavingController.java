package com.fisa.woorionebank.saving.controller;

import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.*;
import com.fisa.woorionebank.saving.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saving")
@RequiredArgsConstructor
public class SavingController {

    private final SavingService savingService;

    @PostMapping
    public ResponseEntity<SavingDTO> createSaving(@RequestBody SavingCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.createSaving(requestDTO));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SavingListDTO> findSavings(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(savingService.findSavings(memberId));
    }

    @PostMapping("/rules")
    public ResponseEntity<SavingRuleDTO> addRule(@RequestBody SavingAddRuleRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.addRule(requestDTO));
    }

    @GetMapping("/rules/{savingId}")
    public ResponseEntity<RuleListDTO> findRules(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.findRules(savingId));
    }

    @PostMapping("/deposit/{ruleId}")
    public ResponseEntity<SavingDTO> deposit(@PathVariable("ruleId") Long ruleId) {
        return ResponseEntity.ok(savingService.ruleDeposit(ruleId));
    }

    @GetMapping("/history/{savingId}")
    public ResponseEntity<SavingHistoryListDTO> findHistory(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.findHistory(savingId));
    }

    @GetMapping("/info/{savingId}")
    public ResponseEntity<SavingInfoDTO> savingInfo(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.savingInfo(savingId));
    }


}
