package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SavingService {
    private final SavingRepository savingRepository;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final CelebrityRepository celebrityRepository;
    private final SavingHistoryRepository savingHistoryRepository;

    /**
     * 최애 적금 생성
     * @param requestDTO
     * @return
     */
    public SavingDTO createSaving(SavingCreateRequestDTO requestDTO) {

        //회원 조회
        final Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));

        // 연예인 조회
        final Celebrity celebrity = celebrityRepository.findById(requestDTO.getCelebrityId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Celebrity));

        // 자동이체 계좌 조회 - 계좌 번호와 MemberId로 조회
        final Account account = accountRepository
                .findByMemberIdAndAccountNumber(requestDTO.getMemberId(),requestDTO.getAccountNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Account));

        // 잔액 확인 - 계좌에 10,000 (생성비용) 보다 많아야함
        long creationCost = 10000L;
        if (account.getBalance().compareTo(BigDecimal.valueOf(creationCost)) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        // 자동이체 계좌에서 생성비용 (10,000원) 차감
        account.setBalance(
                account.getBalance().subtract(BigDecimal.valueOf(creationCost))
        );

        String savingAccount = generateUniqueAccountNumber();

        Saving savedSaving = savingRepository.save(Saving.of(
                requestDTO.getSavingName(),
                savingAccount,
                0,
                LocalDateTime.now().plus(26, ChronoUnit.WEEKS),
                BigDecimal.valueOf(creationCost),
                account,
                member,
                celebrity
        ));

        // 적금 입금 내역 기록 (생성 내역 기록)
        savingHistoryRepository.save(SavingHistory.of(
                TransactionType.CREATION,
                BigDecimal.valueOf(creationCost),
                savedSaving
        ));

        //반환
        return SavingDTO.fromEntity(savedSaving);

    }

    public String generateUniqueAccountNumber() {
        while (true) {
            // 랜덤한 8자리 숫자 생성
            int randomNumber = (int) (Math.random() * 90000000) + 10000000;
            String savingNumber = String.valueOf(randomNumber);

            // 생성한 숫자가 이미 존재하는지 확인
            Optional<Saving> existingAccount = savingRepository.findBySavingAccount(savingNumber);
            // 존재하지 않는다면 해당 숫자 반환
            if (existingAccount.isEmpty()) {
                return savingNumber;
            }
            // 존재한다면 다시 숫자 생성
        }
    }

}
