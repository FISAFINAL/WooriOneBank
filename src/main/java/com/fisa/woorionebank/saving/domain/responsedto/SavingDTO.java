package com.fisa.woorionebank.saving.domain.responsedto;

import com.fisa.woorionebank.saving.domain.entity.DepositDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  반환되는 적금 객체 입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingDTO {
    private Long savingId;
    private String savingName; // 적금이름
    private DepositDay depositDay; // 매주 납입 요일
    private int overdueWeek; // 연체 된 주
    private LocalDateTime endDate; // 적금 만기일
    private int totalAmount; // 총 적금 금액
    private String linkedAccount; // 연동된 계좌
    private String celebrityName; // 연예인 이름
    private String celebrityUrl; // 연예인 사진
    private List<SavingRuleDTO> savingRuleDTOList = new ArrayList<>(); // 규칙 리스트

}
