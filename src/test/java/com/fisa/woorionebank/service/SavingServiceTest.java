package com.fisa.woorionebank.service;


import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.saving.service.SavingService;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class) // 스프링이랑 JUnit 5를 통합해서 스프링 기능을 테스트에 사용가능 하게 하는 어노테이션!
@SpringBootTest
@Transactional
public class SavingServiceTest {
    @Mock
    private SavingRepository savingRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CelebrityRepository celebrityRepository;
    @Mock
    private SavingHistoryRepository savingHistoryRepository;
    @InjectMocks
    private SavingService savingService;
    @BeforeEach
    public void before(){

    }

}