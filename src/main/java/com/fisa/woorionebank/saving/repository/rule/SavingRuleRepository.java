package com.fisa.woorionebank.saving.repository.rule;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.saving.repository.saving.SavingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRuleRepository extends JpaRepository<SavingRule, Long> , SavingRuleRepositoryCustom {

    List<SavingRule> findBySaving(Saving saving);



}
