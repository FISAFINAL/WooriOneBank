package com.fisa.woorionebank.savinghistory.repository;

import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingHistoryRepository extends JpaRepository<SavingHistory,Long> {
}
