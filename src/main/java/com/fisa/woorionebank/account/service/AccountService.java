package com.fisa.woorionebank.account.service;


import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    private final MemberRepository memberRepository;

    public Account createAccount(Long memberId, String bankName) {
        //회원 조회
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.of(bankName, accountNumber, BigDecimal.valueOf(10000), member);
        accountRepository.save(account);

        //연관관계 메소드 호출
        account.setMember(member);

        return account;
    }

    public String generateUniqueAccountNumber() {
        while (true) {
            // 랜덤한 8자리 숫자 생성
            int randomNumber = (int) (Math.random() * 90000000) + 10000000;
            String number = String.valueOf(randomNumber);

            // 생성한 숫자가 이미 존재하는지 확인
            Optional<Account> existingAccount = accountRepository.findByAccountNumber(number);
            // 존재하지 않는다면 해당 숫자 반환
            if (existingAccount.isEmpty()) {
                return number;
            }
            // 존재한다면 다시 숫자 생성
        }
    }
}
