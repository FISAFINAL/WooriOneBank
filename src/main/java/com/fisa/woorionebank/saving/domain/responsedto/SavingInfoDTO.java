package com.fisa.woorionebank.saving.domain.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class SavingInfoDTO {
    private SavingDTO savingDTO;
    private List<SavingHistoryDTO> savingHistoryList = new ArrayList<>();
    private BigDecimal nowInterestRate; // 현재 금리

}
