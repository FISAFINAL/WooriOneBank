package com.fisa.woorionebank.service;


import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.member.domain.dto.requestDto.RegisterDTO;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RegisterDTO registerDTO1 = new RegisterDTO("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        RegisterDTO registerDTO2 = new RegisterDTO("ID2", "PW2", "memeber2", 21, "email2", Grade.VIP);
        RegisterDTO registerDTO3 = new RegisterDTO("ID3", "PW3", "memeber3", 22, "email3", Grade.PLATINUM);
        RegisterDTO registerDTO4 = new RegisterDTO("ID4", "PW4", "memeber4", 23, "email4", Grade.GOLD);
        RegisterDTO registerDTO5 = new RegisterDTO("ID5", "PW5", "memeber5", 24, "email5", Grade.NONE);

        Member member1 = Member.createMember(registerDTO1, passwordEncoder.encode(registerDTO1.getPassword()));
        Member member2 = Member.createMember(registerDTO2, passwordEncoder.encode(registerDTO2.getPassword()));
        Member member3 = Member.createMember(registerDTO3, passwordEncoder.encode(registerDTO3.getPassword()));
        Member member4 = Member.createMember(registerDTO4, passwordEncoder.encode(registerDTO4.getPassword()));
        Member member5 = Member.createMember(registerDTO5, passwordEncoder.encode(registerDTO5.getPassword()));



    }

}