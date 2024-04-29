package com.fisa.woorionebank.account.repository;
import com.fisa.woorionebank.account.entity.Account;

import java.util.Optional;

public interface AccountRepositoryCustom {
    Optional<Account> findByMemberIdAndAccountNumber(Long memberId, String accountNumber);

}
