package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.saving.domain.entity.Saving;
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
    private static final BigDecimal cost = BigDecimal.valueOf(10000);

    /**
     * 매주 10000원씩 입금 (매일 아침 9시에 벌크연산)
     *
     * @param
     * @return
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void weeklyDeposit() {

        // 메소드가 실행되는 요일 계산
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        // 해당 요일에 생성되었고 생성된지 5일 이상 and 주차가 26주 이하인 적금 조회
        List<Saving> results = savingRepository.findSavingByCreatedDayOfweek(today);

        for (Saving saving : results) {
            // 요일을 하루씩 update
            saving.updateCurrentWeek(1);
            try {
                autoDeposit(saving);
            } catch (CustomException e) {
                //첫 연체 주 설정
                if (saving.getOverdueWeek() == 0) {
                    saving.setOverdueWeek(saving.getCurrentWeek());
                }
            }
        }
    }

    private void autoDeposit(Saving saving) {

        // 연동 계좌에서 10,000원 차감
        saving.getAccount().minus(cost);

        // 적금 totalAmount 에 10,000원 추가
        saving.plus(cost);

        // 적금 입금 내역 기록 (자동이체 내역 기록)
        savingHistoryRepository.save(SavingHistory.of(
                TransactionType.CREATION,
                cost,
                saving
        ));
    }
}

/**
 * 생성된 요일로부터 일주일 후 부터 매주 해당 요일 09:00시에 10,000씩 자동 이체 합니다.
 * Saving에 int overdueWeek, currentWeek 를 두고 연체된 주, 현재 주차를 기록합니다.
 * Saving이 생성될때
 *      overdueWeek = 0 : 연체된적 없음을 나타냅니다.
 *      currentWeek = 1 : 생성비용(10,000)이 있기 때문에 첫째주 자동이체는 성공한 것으로 취급합니다.
 * currentWeek는 매주 +1씩 update 됩니다.
 * 통장에 잔고가 없어 연체가 발생하면 overdueWeek는 currentWeek로 기록 됩니다. (연체된 주 기록 : 우대금리를 계산하기 위함)
 *
 *
 * 메소드 실행 프로세스
 * 1. 현재 요일 계산: LocalDateTime.now().getDayOfWeek()를 사용하여 현재 요일을 계산
 * 2. 조건에 맞는 적금 조회: 현재 요일에 생성되었고, 생성된 지 5일 이상이며, 주차가 26주 이하인 적금 계좌를 조회합니다.
 * 3. 적금 계좌 처리:
 *                 각 적금 계좌에 대해 현재 주차를 1주 증가시킵니다.
 *                 연동된 계좌에서 10,000원을 차감합니다. 차감 중 예외가 발생하면 첫 연체 주를 설정합니다.
 *
 */

