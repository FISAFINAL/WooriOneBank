package com.fisa.woorionebank.saving.repository.rule;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingRuleRepository extends JpaRepository<SavingRule, Long> {

    List<SavingRule> findBySaving(Saving saving);
}
