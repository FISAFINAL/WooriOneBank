package com.fisa.woorionebank.saving.entity;

import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "saving")
@Entity
public class Saving {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "saving_id")
    private Long savingId;

    private String savingName;

    private String account;

    @Enumerated(EnumType.STRING)
    private DepositDay depositDay;

    private int overdueWeek;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int totalAmount;

    private String linkedAccount;

    private String savingUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "saving")
    private List<SavingHistory> savings = new ArrayList<>();

    @OneToMany(mappedBy = "saving")
    private List<SavingRule> savingRules = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id")
    private Celebrity celebrity;
}
