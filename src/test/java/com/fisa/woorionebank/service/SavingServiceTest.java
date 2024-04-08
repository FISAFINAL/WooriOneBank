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
import com.fisa.woorionebank.saving.domain.requestdto.SavingCreateRequestDTO;
import com.fisa.woorionebank.saving.domain.responsedto.SavingDTO;
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
//    @Rollback(value = false)
    public void createSaving() throws Exception{
        //given
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        em.persist(member1);
        Account account1 = new Account("우리은행", "1111", BigDecimal.valueOf(100000), member1);
        em.persist(account1);
        Celebrity celebrity1 = new Celebrity("BTS", "url1");
        em.persist(celebrity1);

        //when
        SavingCreateRequestDTO requestDTO = new SavingCreateRequestDTO(
                "울오빠들",
                "우리은행",
                account1.getAccountNumber(),
                "Monday",
                celebrity1.getCelebrityId(),
                member1.getMemberId()
        );

        //회원 조회
        final Member member = memberRepository.findById(member1.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));

        SavingDTO saving = savingService.createSaving(requestDTO);
        //then
        assertThat(saving.getSavingName()).isEqualTo("울오빠들");

    }


}