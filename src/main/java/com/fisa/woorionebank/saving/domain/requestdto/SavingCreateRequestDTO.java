package com.fisa.woorionebank.saving.domain.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 적금을 생성할 때 RequestBody로 넘어오는 DTO입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingCreateRequestDTO {
    private String savingName; // 적금 이름
    private String bankName; // 자동이체할 은행이름
    private String accountNumber; // 자동이체할 계좌번호
//    private String depositDay; // 자동 이체 요일
    private Long celebrityId; // 연예인
    private Long memberId; // 최애 통장 주인
}
