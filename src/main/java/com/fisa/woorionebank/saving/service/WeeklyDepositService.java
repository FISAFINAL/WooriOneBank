package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class WeeklyDepositService {

    private final SavingRepository savingRepository;
    private final SavingHistoryRepository savingHistoryRepository;

    /**
     * 매주 10000원씩 입금 (매일 아침 9시에 벌크연산)
     * @param
     * @return
     */
    @Scheduled(cron = "0 0 9 * * *") // 매일 아침 9시에 실행
    public void weeklyDeposit() {

        // 오늘 요일 계산
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        // 해당 요일에 생성되었고 생성된지 5일 이상 and 주차가 26주 이하인 적금 조회
        List<Saving> results = savingRepository.findSavingByCreatedDayOfweek(today);

        BigDecimal cost = BigDecimal.valueOf(10000);

        for (Saving saving : results) {
            // 요일을 하루씩 update
            saving.updateCurrentWeek(1);
            try {
                saving.getAccount().minus(cost);
            } catch (CustomException e) {
                //첫 연체 주 설정
                if (saving.getOverdueWeek() == 0) {
                    saving.setOverdueWeek(saving.getCurrentWeek());
                }
            }
        }



    }
}
