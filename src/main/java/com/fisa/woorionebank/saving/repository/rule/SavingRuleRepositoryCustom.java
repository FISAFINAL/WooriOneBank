package com.fisa.woorionebank.saving.repository.rule;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;

import java.util.List;

public interface SavingRuleRepositoryCustom {

    List<SavingRule> QueryfindBySaving(Saving saving);
}
