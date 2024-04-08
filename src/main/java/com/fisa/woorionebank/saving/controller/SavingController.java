package com.fisa.woorionebank.saving.controller;

import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.RuleListDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingRuleDTO;
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

    @PostMapping("/rules")
    public ResponseEntity<SavingRuleDTO> addRule(@RequestBody SavingAddRuleRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.addRule(requestDTO));
    }

    @GetMapping("rules")
    public ResponseEntity<RuleListDTO> findRules(@PathVariable("savingId") Long savingId) {
        return ResponseEntity.ok(savingService.findRules(savingId));
    }
}
