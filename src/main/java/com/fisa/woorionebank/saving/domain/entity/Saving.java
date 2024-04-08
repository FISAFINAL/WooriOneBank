package com.fisa.woorionebank.saving.domain.entity;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String savingAccount; // 적금 계좌 번호

    private int overdueWeek; // 연체 된 주

    private LocalDateTime endDate; // 적금 만기일

    private BigDecimal totalAmount; // 총 적금 금액

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account; // 연동된 계좌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celebrity_id")
    private Celebrity celebrity;

    @Builder
    public Saving(
            String savingName,
            String savingAccount,
            int overdueWeek,
            LocalDateTime endDate,
            BigDecimal totalAmount,
            Account account,
            Member member,
            Celebrity celebrity
    ) {
        this.savingName = savingName;
        this.savingAccount = savingAccount;
        this.overdueWeek = overdueWeek;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.account = account;
        this.member = member;
        this.celebrity = celebrity;
    }


    public static Saving of(
            String savingName,
            String savingAccount,
            int overdueWeek,
            LocalDateTime endDate,
            BigDecimal totalAmount,
            Account account,
            Member member,
            Celebrity celebrity
    ) {
        return Saving.builder()
                .savingName(savingName)
                .savingAccount(savingAccount)
                .overdueWeek(overdueWeek)
                .endDate(endDate)
                .totalAmount(totalAmount)
                .account(account)
                .member(member)
                .celebrity(celebrity)
                .build();
    }

    //연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getSavings().add(this);
    }

}
