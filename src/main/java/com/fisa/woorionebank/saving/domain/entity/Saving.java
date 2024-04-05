package com.fisa.woorionebank.saving.domain.entity;

import com.fisa.woorionebank.common.BaseEntity;
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
public class Saving extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "saving_id")
    private Long savingId;

    private String savingName;

    private String account; // 적금 계좌 번호

    @Enumerated(EnumType.STRING)
    private DepositDay depositDay; // 매주 납입 요일

    private int overdueWeek; // 연체 된 주

    private LocalDateTime endDate; // 적금 만기일

    private int totalAmount; // 총 적금 금액

    private String linkedAccount; // 연동된 계좌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "saving")
    private List<SavingHistory> savingHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "saving")
    private List<SavingRule> savingRuleList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id")
    private Celebrity celebrity;
}
