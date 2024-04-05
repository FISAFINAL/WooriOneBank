package com.fisa.woorionebank.saving.domain.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int depositAmount;
}
