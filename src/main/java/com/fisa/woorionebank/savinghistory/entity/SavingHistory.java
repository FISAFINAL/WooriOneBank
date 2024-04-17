package com.fisa.woorionebank.savinghistory.entity;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "saving_history")
@Entity
public class SavingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_history_id")
    private Long savingHistoryId;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id")
    private Saving saving;


    @Builder
    public SavingHistory(TransactionType transactionType, BigDecimal amount, Saving saving) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.saving = saving;
    }

    public static SavingHistory of(
            TransactionType transactionType,
            BigDecimal amount,
            Saving saving
    ){
        return SavingHistory.builder()
                .transactionType(transactionType)
                .amount(amount)
                .saving(saving)
                .build();
    }

}
