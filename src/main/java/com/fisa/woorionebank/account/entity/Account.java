package com.fisa.woorionebank.account.entity;

import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "account")
@Entity
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long accountId;

    private String bankName;

    private String accountNumber;

    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Account(
            String bankName,
            String accountNumber,
            BigDecimal balance,
            Member member
    ) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.member = member;
    }

    public static Account of(
            String bankName,
            String accountNumber,
            BigDecimal balance,
            Member member
    ) {
        return Account.builder()
                .bankName(bankName)
                .accountNumber(accountNumber)
                .balance(balance)
                .member(member)
                .build();
    }

    //연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getAccounts().add(this);
    }

    // 잔액 감소 메서드
    public void minus(BigDecimal amount) {
        // 현재 잔액보다 출금액이 크면 예외 발생
        if (this.balance.compareTo(amount) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
        }
        // 출금액을 잔액에서 차감
        this.balance = this.balance.subtract(amount);
    }



}
