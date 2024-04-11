package com.fisa.woorionebank.service;


import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.account.service.AccountService;
import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.domain.dto.requestDto.RegisterDTO;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.DepositDay;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.requestdto.SavingAddRuleRequestDTO;
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingRuleDTO;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.saving.service.SavingService;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    public void 최애적금생성() throws Exception{
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

    }

    @Test
    public void 최애적금규칙추가() throws Exception{
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


}