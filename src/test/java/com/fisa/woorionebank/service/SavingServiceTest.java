package com.fisa.woorionebank.service;


import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.account.service.AccountService;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.*;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.saving.service.SavingService;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class) // 스프링이랑 JUnit 5를 통합해서 스프링 기능을 테스트에 사용가능 하게 하는 어노테이션!
@SpringBootTest
@Transactional
public class SavingServiceTest {
    @Autowired
    private SavingRepository savingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CelebrityRepository celebrityRepository;
    @Autowired
    private SavingHistoryRepository savingHistoryRepository;
    @Autowired
    private SavingService savingService;
    @Autowired
    private AccountService accountService;

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @Test
    public void 최애적금생성_createSaving() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);
        Account account1 = new Account("우리은행", "1111", BigDecimal.valueOf(100000), member1);
        em.persist(account1);
        member1.addAccount(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        //when
        SavingCreateRequestDTO requestDTO = new SavingCreateRequestDTO(
                "울오빠들",
                "우리은행",
                account1.getAccountNumber(),
                celebrity1.getCelebrityId(),
                member1.getMemberId()
        );
        SavingDTO savingDTO = savingService.createSaving(requestDTO);

        //then
        assertThat(savingDTO.getSavingName()).isEqualTo("울오빠들");
        assertThat(account1.getBalance()).isEqualTo(BigDecimal.valueOf(90_000));

    }

    @Test
    public void 최애적금조회_findSavings() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(100000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10000),
                account1,
                member1,
                celebrity1
        );
        Saving saving2 = new Saving(
                "울아빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);
        em.persist(saving2);
        member1.addSaving(saving1);
        member1.addSaving(saving2);

        //when
        SavingListDTO savings = savingService.findSavings(member1.getMemberId());

        //then
        assertThat(savings.getSavingList().size()).isEqualTo(2);
    }

    @Test
    public void 최애적금규칙추가_addRule() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(100000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);

        //when
        SavingAddRuleRequestDTO requestDTO = new SavingAddRuleRequestDTO(
                "우리오빠 사진찍을때",
                BigDecimal.valueOf(1000),
                saving1.getSavingId()
        );

        SavingRuleDTO savingRuleDTO = savingService.addRule(requestDTO);

        //then
        assertThat(savingRuleDTO.getSavingRuleName()).isEqualTo("우리오빠 사진찍을때");

    }

    @Test
    public void 적금규칙조회_findRules() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(100000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);
        SavingRule savingRule1 = new SavingRule("울 오빠 셀카", BigDecimal.valueOf(10000), saving1);
        SavingRule savingRule2 = new SavingRule("울 아빠 셀카", BigDecimal.valueOf(10000), saving1);
        em.persist(savingRule1);
        em.persist(savingRule2);

        //when
        RuleListDTO ruleListDTO = savingService.findRules(saving1.getSavingId());

//        System.out.println(ruleListDTO.getSavingRuleList().get(1).getSavingRuleName());

        //then
        assertThat(ruleListDTO.getSavingRuleList().size()).isEqualTo(2);
    }

    @Test
    public void 규칙입금_ruleDeposit() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(100_000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10_000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);
        SavingRule savingRule1 = new SavingRule("울 오빠 셀카", BigDecimal.valueOf(10_000), saving1);
        em.persist(savingRule1);

        //when
        SavingDTO savingDTO = savingService.ruleDeposit(savingRule1.getSavingRuleId());

        //then
        assertThat(savingDTO.getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)); // 생성비용 + 10,000
        assertThat(account1.getBalance()).isEqualTo(BigDecimal.valueOf(90_000)); // 계좌에선 돈빠짐

    }

    @Test
    public void 규칙입금_ruleDeposit_실패() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(5_000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                1,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10_000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);
        SavingRule savingRule1 = new SavingRule("울 오빠 셀카", BigDecimal.valueOf(10_000), saving1);
        em.persist(savingRule1);

        //then
        assertThatThrownBy(() -> savingService.ruleDeposit(savingRule1.getSavingRuleId()))
                .isInstanceOf(CustomException.class);
    }


    @Test
    public void 입금내역조회_findHistory() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(100_000), member1);
        em.persist(account1);
        member1.addAccount(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        //when
        SavingCreateRequestDTO requestDTO = new SavingCreateRequestDTO(
                "울오빠들",
                "우리은행",
                account1.getAccountNumber(),
                celebrity1.getCelebrityId(),
                member1.getMemberId()
        );
        SavingDTO savingDTO = savingService.createSaving(requestDTO);

        Saving saving1 = savingRepository.findById(savingDTO.getSavingId()).get();

        SavingRule savingRule1 = new SavingRule("울 오빠 셀카", BigDecimal.valueOf(10_000), saving1);
        em.persist(savingRule1);
        savingService.ruleDeposit(savingRule1.getSavingRuleId());

        //when
        SavingHistoryListDTO history = savingService.findHistory(saving1.getSavingId());

        //then
        assertThat(history.getSavingHistoryList().get(0).getTransactionType()).isEqualTo(TransactionType.CREATION);
        assertThat(history.getSavingHistoryList().get(1).getTransactionType()).isEqualTo(TransactionType.FREE);
        assertThat(history.getSavingHistoryList().get(1).getAmount()).isEqualTo(BigDecimal.valueOf(10_000));
    }

    @Test
    public void 금리조회_savingInfo1() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(5_000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                0,
                26,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10_000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);

        SavingInfoDTO savingInfoDTO = savingService.savingInfo(saving1.getSavingId());
        //then
        assertThat(savingInfoDTO.getNowInterestRate()).isEqualTo(BigDecimal.valueOf(7.00));
    }

    @Test
    public void 금리조회_savingInfo2() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(5_000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                2,
                26,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10_000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);

        SavingInfoDTO savingInfoDTO = savingService.savingInfo(saving1.getSavingId());
        //then
        assertThat(savingInfoDTO.getNowInterestRate()).isEqualTo(BigDecimal.valueOf(3.50));
    }

    @Test
    public void 금리조회_savingInfo3() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);

        Account account1 = new Account("우리은행", "통장계좌번호", BigDecimal.valueOf(5_000), member1);
        em.persist(account1);

        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        Saving saving1 = new Saving(
                "울오빠들",
                "적금계좌번호",
                9,
                26,
                LocalDateTime.now().plusWeeks(26),
                BigDecimal.valueOf(10_000),
                account1,
                member1,
                celebrity1
        );
        em.persist(saving1);

        SavingInfoDTO savingInfoDTO = savingService.savingInfo(saving1.getSavingId());
        //then
        assertThat(savingInfoDTO.getNowInterestRate()).isEqualTo(BigDecimal.valueOf(4.50));
    }




}