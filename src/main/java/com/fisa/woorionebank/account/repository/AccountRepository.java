package com.fisa.woorionebank.account.repository;

import com.fisa.woorionebank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long>, AccountRepositoryCustom {
    Optional<Account> findByAccountNumber(String accountNumber);

}
