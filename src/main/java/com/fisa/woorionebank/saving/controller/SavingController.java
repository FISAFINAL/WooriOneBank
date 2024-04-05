package com.fisa.woorionebank.saving.controller;

import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saving")
@RequiredArgsConstructor
public class SavingController {

    private final SavingService savingService;

    @PostMapping
    public ResponseEntity<SavingDTO> createSaving(@RequestBody SavingCreateRequestDTO requestDTO) {
        return ResponseEntity.ok(savingService.createSaving(requestDTO));
    }

}
