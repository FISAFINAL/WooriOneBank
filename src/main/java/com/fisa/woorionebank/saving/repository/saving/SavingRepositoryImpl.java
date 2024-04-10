package com.fisa.woorionebank.saving.repository.saving;


import com.fisa.woorionebank.saving.domain.entity.QSaving;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static com.fisa.woorionebank.saving.domain.entity.QSaving.*;

//구현
@RequiredArgsConstructor
public class SavingRepositoryImpl implements SavingRepositoryCustom{

    //복잡한 쿼리 구현

    private final JPAQueryFactory queryFactory;


    /**
     * 현재 요일에 생성되었고, 생성된 지 5일 이상이며, 주차가 26주 이하인 적금 계좌를 조회
     * @param today
     * @return
     */
    @Override
    public List<Saving> findSavingByCreatedDayOfweek(DayOfWeek today) {
        // 요일을 문자열로 변환
        String todayString = today.toString();
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);

        return queryFactory.selectFrom(saving)
                .where(
                        saving.createdTime.dayOfWeek().stringValue().eq(todayString)
                                .and(saving.createdTime.before(fiveDaysAgo))
                                .and(saving.currentWeek.loe(26)) // currentWeek가 26 이하인 경우
                )
                .fetch();
    }



}
