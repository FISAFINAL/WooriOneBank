package com.fisa.woorionebank.saving.domain.responsedto;

import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SavingHistoryDTO {
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public static SavingHistoryDTO fromEntity(SavingHistory savingHistory) {
        return SavingHistoryDTO.builder()
                .transactionType(savingHistory.getTransactionType())
                .amount(savingHistory.getAmount())
                .createdAt(savingHistory.getSaving().getCreatedTime())
                .build();
    }

}
