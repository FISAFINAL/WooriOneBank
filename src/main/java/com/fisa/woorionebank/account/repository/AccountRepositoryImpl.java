package com.fisa.woorionebank.account.repository;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.entity.QAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QAccount qAccount = QAccount.account;


    @Override
    public Optional<Account> findByMemberIdAndAccountNumber(Long memberId, String accountNumber) {
        
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(qAccount)
                .where(qAccount.member.memberId.eq(memberId)
                        .and(qAccount.accountNumber.eq(accountNumber)))
                .fetchOne());
    }
}
