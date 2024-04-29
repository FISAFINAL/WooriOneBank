package com.fisa.woorionebank.saving.domain.responsedto;

import com.fisa.woorionebank.saving.domain.entity.DepositDay;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private Long savingId; // 적금Id
    private String savingName; // 적금이름
    private LocalDateTime endDate; // 적금 만기일
    private BigDecimal totalAmount; // 총 적금 금액
    private String celebrityName; // 연예인 이름
    private String celebrityUrl; // 연예인 사진

    // Saving -> SavingDTO
    public static SavingDTO fromEntity(Saving saving) {
        return SavingDTO.builder()
                .savingId(saving.getSavingId())
                .savingName(saving.getSavingName())
                .endDate(saving.getEndDate())
                .totalAmount(saving.getTotalAmount())
                .celebrityName(saving.getCelebrity().getCelebrityName())
                .celebrityUrl(saving.getCelebrity().getCelebrityUrl())
                .build();
    }

}
