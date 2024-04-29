package com.fisa.woorionebank.savinghistory.repository;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingHistoryRepository extends JpaRepository<SavingHistory,Long> {
    List<SavingHistory> findBySaving(Saving saving);
}
