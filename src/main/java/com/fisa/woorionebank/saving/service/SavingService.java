package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.*;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.rule.SavingRuleRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final SavingRuleRepository savingRuleRepository;

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

        // 자동이체 계좌 조회 - Member 엔티티에서 직접 찾음 (양방향)
        final Account account = member.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(requestDTO.getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Account));

//        final Account account1 = accountRepository.findByMember(member)


        // 잔액 확인 - 계좌에 10,000 (생성비용) 보다 많아야함
        BigDecimal creationCost = BigDecimal.valueOf(10000);

        // 자동이체 계좌에서 생성비용 (10,000원) 차감
        account.minus(creationCost);

        String savingAccount = generateUniqueAccountNumber();

        Saving savedSaving = savingRepository.save(Saving.of(
                requestDTO.getSavingName(),
                savingAccount,
                0,
                1,
                LocalDateTime.now().plus(26, ChronoUnit.WEEKS),
                creationCost,
                account,
                member,
                celebrity
        ));

        member.addSaving(savedSaving);

        // 적금 입금 내역 기록 (생성 내역 기록)
        savingHistoryRepository.save(SavingHistory.of(
                TransactionType.CREATION,
                creationCost,
                savedSaving
        ));

        //반환
        return SavingDTO.fromEntity(savedSaving);

    }

    /**
     * 최애 적금 조회
     */
    public SavingListDTO findSavings(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));

        List<Saving> savingList = member.getSavings();

        List<SavingDTO> savingDTOList = savingList.stream()
                .map(SavingDTO::fromEntity)
                .collect(Collectors.toList());

        return new SavingListDTO(savingDTOList);
    }

    /**
     * 최애 적금에 규칙 추가
     * @param requestDTO
     * @return
     */
    public SavingRuleDTO addRule(SavingAddRuleRequestDTO requestDTO) {

        // 적금 조회
        final Saving saving = savingRepository.findById(requestDTO.getSavingId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        SavingRule savingRule = savingRuleRepository.save(SavingRule.of(
                requestDTO.getSavingRuleName(),
                requestDTO.getDepositAmount(),
                saving
        ));

        return SavingRuleDTO.fromEntity(savingRule);
    }

    /**
     * 최애 적금 규칙 조회
     *
     * @return
     */
    public RuleListDTO findRules(Long savingId) {
        // 계좌 조회
        final Saving saving = savingRepository.findById(savingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        List<SavingRule> ruleList = savingRuleRepository.findBySaving(saving);

        // List<SavingRule> -> List<SavingRuleDTO> 변환
        List<SavingRuleDTO> ruleDTOList = ruleList.stream()
                .map(SavingRuleDTO::fromEntity)
                .collect(Collectors.toList());

        return new RuleListDTO(ruleDTOList);
    }


    /**
     * 최애적금으로 입금 By 규칙
     *
     * @return
     */
    public SavingDTO ruleDeposit(Long savingRuleId) {

        // 규칙 조회
        final SavingRule savingRule = savingRuleRepository.findById(savingRuleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SavingRule));

        Saving saving = savingRule.getSaving();
        Account account = savingRule.getSaving().getAccount();
        BigDecimal depositAmount = savingRule.getDepositAmount();

        // 자동이체 계좌에서 비용차감
        account.minus(depositAmount);

        // 적금에 입금
        saving.plus(depositAmount);

        // 적금 입금 내역 기록 (자유 규칙입금 내역 기록)
        savingHistoryRepository.save(SavingHistory.of(
                TransactionType.FREE,
                depositAmount,
                saving
        ));

        //반환
        return SavingDTO.fromEntity(saving);

    }


    /**
     * 최애적금 입금내역 조회
     *
     * @return
     */
    public SavingHistoryListDTO findHistory(Long savingId) {

        final Saving saving = savingRepository.findById(savingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        List<SavingHistory> ruleList = savingHistoryRepository.findBySaving(saving);

        List<SavingHistoryDTO> historyDTOList = ruleList.stream()
                .map(SavingHistoryDTO::fromEntity)
                .collect(Collectors.toList());

        return new SavingHistoryListDTO(historyDTOList);

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
