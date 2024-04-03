package com.fisa.woorionebank.savinghistory.entity;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.saving.entity.Saving;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "saving_history")
@Entity
public class SavingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "saving_history_id")
    private Long savingHistoryId;

    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id")
    private Saving saving;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}
