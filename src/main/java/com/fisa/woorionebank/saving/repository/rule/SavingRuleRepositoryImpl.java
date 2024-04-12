package com.fisa.woorionebank.saving.repository.rule;

import com.fisa.woorionebank.saving.domain.entity.QSavingRule;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.saving.repository.saving.SavingRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

import static com.fisa.woorionebank.saving.domain.entity.QSavingRule.*;

@RequiredArgsConstructor
public class SavingRuleRepositoryImpl implements SavingRuleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SavingRule> QueryfindBySaving(Saving saving) {
        return queryFactory
                .selectFrom(savingRule)
                .where(savingRule.saving.eq(saving))
                .fetch();
    }

}
