package com.fisa.woorionebank.saving.domain.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 최애 적금에 규칙을 추가할때 RequestBody로 넘어오는 DTO입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingAddRuleRequestDTO {
    private String savingRuleName; // 규칙 이름
    private BigDecimal depositAmount; // 규칙 입금 금액
    private Long savingId; // 최애 적금 id
}
