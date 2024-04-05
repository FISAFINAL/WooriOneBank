package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.repository.SavingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SavingService {
    private final SavingRepository savingRepository;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    /**
     * 최애 적금 생성
     * @param requestDTO
     * @return
     */
//    public SavingDTO create(SavingCreateRequestDTO requestDTO) {
//
//        //회원 조회
//        final Member member = memberRepository.findById(requestDTO.getMemberId())
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));
//
//        // 자동이체 계좌 조회 - 계좌 번호로 조회
//        final Account account = accountRepository.findByAccountNumber(requestDTO.getAccountNumber())
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Account));
//
//        // 잔액 확인 - 계좌에 10,000 (생성비용) 보다 많아야함
//        long creationCost = 10000L;
//        if (account.getBalance().compareTo(BigDecimal.valueOf(creationCost)) < 0) {
//            throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
//        }
//
//        // 자동이체 계좌에서 생성비용 (10,000원) 차감
//        account.setBalance(
//                account.getBalance().subtract(BigDecimal.valueOf(creationCost))
//        );
//
//        // 적금 거래 내역 기록
//
//    }

}
