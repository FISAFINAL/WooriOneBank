package com.fisa.woorionebank.saving.repository.saving;

import com.fisa.woorionebank.saving.domain.entity.Saving;

import java.time.DayOfWeek;
import java.util.List;

public interface SavingRepositoryCustom {
//    복잡한 쿼리
    List<Saving> findSavingByCreatedDayOfweek(DayOfWeek today);

}
