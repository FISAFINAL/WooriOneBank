package com.fisa.woorionebank.saving.domain.entity;

import com.fisa.woorionebank.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "saving_rule")
@Entity
public class SavingRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_rule_id")
    private Long savingRuleId;

    private String savingRuleName;

    private BigDecimal depositAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id")
    private Saving saving;

    @Builder
    public SavingRule(
            String savingRuleName,
            BigDecimal depositAmount,
            Saving saving
    ) {
        this.savingRuleName = savingRuleName;
        this.depositAmount = depositAmount;
        this.saving = saving;
    }

    public static SavingRule of(
            String savingRuleName,
            BigDecimal depositAmount,
            Saving saving
    ) {
        return SavingRule.builder()
                .savingRuleName(savingRuleName)
                .depositAmount(depositAmount)
                .saving(saving)
                .build();
    }
}