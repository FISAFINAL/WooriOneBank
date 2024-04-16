package com.fisa.woorionebank.dumy;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class) // 스프링이랑 JUnit 5를 통합해서 스프링 기능을 테스트에 사용가능 하게 하는 어노테이션!
@SpringBootTest
@Transactional
public class Dummy {

    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 테스트데이터삽입() throws Exception{
        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        Member member2 = new Member("ID2", "PW2", "memeber2", 21, "email2", Grade.VIP);
        Member member3 = new Member("ID3", "PW3", "memeber3", 22, "email3", Grade.PLATINUM);
        Member member4 = new Member("ID4", "PW4", "memeber4", 23, "email4", Grade.GOLD);
        Member member5 = new Member("ID5", "PW5", "memeber5", 24, "email5", Grade.NONE);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);

        Account account1 = Account.of("우리은행", "10041111", BigDecimal.valueOf(1_000_000), member1);
        Account account2 = Account.of("국민은행", "10042222", BigDecimal.valueOf(1_000_000), member1);
        Account account3 = Account.of("우리은행", "10043333", BigDecimal.valueOf(100_000), member2);
        Account account4 = Account.of("우리은행", "10044444", BigDecimal.valueOf(100_000), member3);
        em.persist(account1);
        em.persist(account2);
        em.persist(account3);
        em.persist(account4);

        Celebrity celebrity1 =  Celebrity.of("BTS", "url1");
        Celebrity celebrity2 =  Celebrity.of("EXO", "url2");
        em.persist(celebrity1);
        em.persist(celebrity2);

        //최애적금
        Saving saving1 = Saving.of(
                "울오빠",
                "1111",
                0,
                5,
                LocalDateTime.now().plus(26, ChronoUnit.WEEKS),
                BigDecimal.valueOf(10_000),
                account1, member1, celebrity1);

        Saving saving2 = Saving.of(
                "울애귀",
                "2222",
                0,
                26,
                LocalDateTime.now().plus(26, ChronoUnit.WEEKS),
                BigDecimal.valueOf(10_000),
                account1, member1, celebrity2);
        em.persist(saving1);
        em.persist(saving2);

        // 최애규칙
        SavingRule savingRule1 = SavingRule.of("울오빠 셀카", BigDecimal.valueOf(1_000), saving1);
        SavingRule savingRule2 = SavingRule.of("울오빠 라방", BigDecimal.valueOf(2_000), saving1);
        SavingRule savingRule3 = SavingRule.of("울오빠 스토리", BigDecimal.valueOf(1_500), saving1);
        em.persist(savingRule1);
        em.persist(savingRule2);
        em.persist(savingRule3);

        // 입금 내역
        SavingHistory savingHistory1 = SavingHistory.of(TransactionType.CREATION, BigDecimal.valueOf(10_000), saving1);
        SavingHistory savingHistory2 = SavingHistory.of(TransactionType.FREE, BigDecimal.valueOf(1_000), saving1);
        SavingHistory savingHistory3 = SavingHistory.of(TransactionType.FREE, BigDecimal.valueOf(2_000), saving1);
        SavingHistory savingHistory4 = SavingHistory.of(TransactionType.WEEK, BigDecimal.valueOf(10_000), saving1);
        em.persist(savingHistory1);
        em.persist(savingHistory2);
        em.persist(savingHistory3);
        em.persist(savingHistory4);
    }
}
