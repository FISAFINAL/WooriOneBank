package com.fisa.woorionebank.saving.service;

import com.fisa.woorionebank.account.entity.Account;
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
import com.fisa.woorionebank.saving.util.AccountNumberGenerator;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SavingService {

    //test
    private final SavingRepository savingRepository;
    private final MemberRepository memberRepository;
    private final CelebrityRepository celebrityRepository;
    private final SavingHistoryRepository savingHistoryRepository;
    private final SavingRuleRepository savingRuleRepository;

    private Set<String> existingAccountNumbers = new HashSet<>();
    // 계좌번호를 생성할때 중복체크를 위해 테이블을 모두 읽는게 비효율적
    // 많이는 안겹치겠지만 쿼리 몇번 나갈지 예측을 못하지 않나

    private static final BigDecimal CREATION_COST = BigDecimal.valueOf(10_000);


    /**
     * 최애적금 생성
     * @param requestDTO
     * @return
     */
    @Transactional
    public SavingDTO createSaving(SavingCreateRequestDTO requestDTO) {

        //회원 및 계좌 조회 (추후 fetchJoin으로 개선)
        final Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));
        // 자동이체 계좌 조회 - Member 엔티티에서 직접 찾음 (양방향)
        final Account account = member.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(requestDTO.getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Account));

        // 연예인 조회
        final Celebrity celebrity = celebrityRepository.findById(requestDTO.getCelebrityId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Celebrity));


        // 랜덤 계좌번호 등록
        String savingAccount = AccountNumberGenerator.generateUniqueAccountNumber(existingAccountNumbers);
        // 중복확인을 위해 DB까지 쿼리를 날리지 않도록 하기위함
        existingAccountNumbers.add(savingAccount);

        Saving savedSaving = savingRepository.save(Saving.of(
                requestDTO.getSavingName(),
                savingAccount,
                0,
                1,
                LocalDateTime.now().plus(26, ChronoUnit.WEEKS),
                BigDecimal.ZERO,
                account,
                member,
                celebrity
        ));
        //연관관계 설정 메소드
        member.addSaving(savedSaving);

        // 생성비용 지불
        deposit(account,savedSaving,CREATION_COST,TransactionType.CREATION);

        //반환
        return SavingDTO.fromEntity(savedSaving);

    }

    /**
     * 최애 적금 조회
     */
    public SavingListDTO findSavings(Long memberId) {

        // 추후 fetchJoin으로 개선
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));

        // Saving : Member 다대이 양방향 연관관계
        List<Saving> savingList = member.getSavings();

        // List<Saving> ->  List<SavingDTO> 변환
        List<SavingDTO> savingDTOList = savingList.stream()
                .map(SavingDTO::fromEntity)
                .collect(Collectors.toList());

        return new SavingListDTO(savingDTOList);
    }
    @Transactional
    public SavingListDTO findSaving(Member member) {

        // Saving : Member 다대이 양방향 연관관계
        List<Saving> savingList = member.getSavings();

        // List<Saving> ->  List<SavingDTO> 변환
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
    @Transactional
    public SavingRuleDTO addRule(SavingAddRuleRequestDTO requestDTO) {

        // 적금 조회
        final Saving saving = savingRepository.findById(requestDTO.getSavingId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        // 최애적금 규칙추가
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

        // 규칙조회 (규칙 없을 수 있음)
//        List<SavingRule> ruleList = savingRuleRepository.findBySaving(saving);
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
    @Transactional
    public SavingDTO ruleDeposit(Long savingRuleId) {

        // 규칙 조회
        final SavingRule savingRule = savingRuleRepository.findById(savingRuleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SavingRule));

        Saving saving = savingRule.getSaving();
        Account account = savingRule.getSaving().getAccount();
        BigDecimal depositAmount = savingRule.getDepositAmount();

        // 계좌(-), 적금(+), 금액, 타입
        deposit(account,saving,depositAmount,TransactionType.FREE);

        //반환
        return SavingDTO.fromEntity(saving);

    }


    /**
     * 최애적금 입금내역 조회
     *
     * @return
     */
    public SavingHistoryListDTO findHistory(Long savingId) {

        // 적금 조회
        final Saving saving = savingRepository.findById(savingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        // 규칙리스트 조회
        List<SavingHistory> historyList = savingHistoryRepository.findBySaving(saving);

        // List<SavingHistory> -> List<SavingHistoryDTO> 변환
        List<SavingHistoryDTO> historyDTOList = historyList.stream()
                .map(SavingHistoryDTO::fromEntity)
                .collect(Collectors.toList());

        return new SavingHistoryListDTO(historyDTOList);

    }


    /**
     * 최애적금 상세 보기
     * @param savingId
     * @return
     */
    public SavingInfoDTO savingInfo(Long savingId) {

        //적금조회
        final Saving saving = savingRepository.findById(savingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Saving));

        // SavingDTO 생성
        SavingDTO savingDTO = SavingDTO.fromEntity(saving);

        // 입금 내역 조회
        List<SavingHistory> ruleList = savingHistoryRepository.findBySaving(saving);

        // List<SavingHistory> -> List<SavingHistoryDTO> 변환
        List<SavingHistoryDTO> historyDTOList = ruleList.stream()
                .map(SavingHistoryDTO::fromEntity)
                .collect(Collectors.toList());

        // 금리 계산
        BigDecimal nowInterestRate = calculateRate(saving.getOverdueWeek(), saving.getCurrentWeek());

        return new SavingInfoDTO(savingDTO, historyDTOList, nowInterestRate);

    }


    // == Service private 메소드 == //


    private void deposit(Account account, Saving saving, BigDecimal depositAmount,TransactionType transactionType ) {
        // 자동이체 계좌에서 비용 차감
        account.minus(depositAmount);

        // 적금에 입금
        saving.plus(depositAmount);

        // 적금 입금 내역 기록
        savingHistoryRepository.save(SavingHistory.of(
                transactionType,
                depositAmount,
                saving
        ));
    }



    private BigDecimal calculateRate(int overdueWeek, int currentWeek) {
        if (currentWeek >= 1 && currentWeek <= 7) {
            return BigDecimal.valueOf(3.50);
        } else if (currentWeek >= 8 && currentWeek <= 26) {
            return calculateRateWithinRange(overdueWeek);
        } else if (currentWeek > 26) {
            return calculateRateExceedRange(overdueWeek);
        } else {
            throw new CustomException(ErrorCode.INVALID_OVERDUE_WEEK);
        }
    }

    private BigDecimal calculateRateWithinRange(int overdueWeek) {
        if (overdueWeek >= 1 && overdueWeek <= 7) {
            return BigDecimal.valueOf(3.50);
        } else if (overdueWeek >= 8 && overdueWeek <= 26) {
            return BigDecimal.valueOf(4.50);
        } else if (overdueWeek == 0) {
            return BigDecimal.valueOf(4.50);
        } else {
            throw new CustomException(ErrorCode.INVALID_OVERDUE_WEEK);
        }
    }

    private BigDecimal calculateRateExceedRange(int overdueWeek) {
        if (overdueWeek >= 1 && overdueWeek <= 7) {
            return BigDecimal.valueOf(3.50);
        } else if (overdueWeek >= 8 && overdueWeek <= 26) {
            return BigDecimal.valueOf(4.50);
        } else if (overdueWeek == 0) {
            return BigDecimal.valueOf(7.00);
        } else {
            throw new CustomException(ErrorCode.INVALID_OVERDUE_WEEK);
        }
    }


}
