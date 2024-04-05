package com.fisa.woorionebank.saving.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "saving_rule")
@Entity
public class SavingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "saving_rule_id")
    private Long savingRuleId;

    private String savingRuleName;

    private int depositAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id")
    private Saving saving;

}