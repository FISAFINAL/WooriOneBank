package com.fisa.woorionebank.saving.domain.responsedto;

import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *  반환되는 적금 규칙 객체 입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingRuleDTO {
    private Long savingRuleId;
    private String savingRuleName;
    private BigDecimal depositAmount;

    public static SavingRuleDTO fromEntity(SavingRule savingRule) {
        return SavingRuleDTO.builder()
                .savingRuleId(savingRule.getSavingRuleId())
                .savingRuleName(savingRule.getSavingRuleName())
                .depositAmount(savingRule.getDepositAmount())
                .build();
    }
}
