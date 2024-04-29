package com.fisa.woorionebank.dumy;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.account.repository.AccountRepository;
import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.ConcertVenue;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertVenueRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.SavingRule;
import com.fisa.woorionebank.saving.repository.celebrity.CelebrityRepository;
import com.fisa.woorionebank.saving.repository.rule.SavingRuleRepository;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import com.fisa.woorionebank.savinghistory.entity.SavingHistory;
import com.fisa.woorionebank.savinghistory.entity.TransactionType;
import com.fisa.woorionebank.savinghistory.repository.SavingHistoryRepository;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.entity.SeatClass;
import com.fisa.woorionebank.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Dumy {

    private final MemberRepository memberRepository;

    private final AccountRepository accountRepository;

    private final CelebrityRepository celebrityRepository;

    private final SavingRepository savingRepository;

    private final SavingRuleRepository savingRuleRepository;

    private final SavingHistoryRepository savingHistoryRepository;

    private final ConcertRepository concertRepository;

    private final ConcertVenueRepository concertVenueRepository;

    private final ConcertHistoryRepository concertHistoryRepository;

    private final SeatRepository seatRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB(){
        initData();
    }

    public void initData() {

        Member member1 = new Member("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
        Member member2 = new Member("ID2", "PW2", "memeber2", 21, "email2", Grade.VIP);
        Member member3 = new Member("ID3", "PW3", "memeber3", 22, "email3", Grade.PLATINUM);
        Member member4 = new Member("ID4", "PW4", "memeber4", 23, "email4", Grade.GOLD);
        Member member5 = new Member("ID5", "PW5", "memeber5", 24, "email5", Grade.NONE);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Account account1 = Account.of("우리은행", "10041111", BigDecimal.valueOf(1_000_000), member1);
        Account account2 = Account.of("국민은행", "10042222", BigDecimal.valueOf(1_000_000), member1);
        Account account3 = Account.of("우리은행", "10043333", BigDecimal.valueOf(100_000), member2);
        Account account4 = Account.of("우리은행", "10044444", BigDecimal.valueOf(100_000), member3);

        member1.addAccount(account1);
        member1.addAccount(account2);
        member2.addAccount(account3);
        member3.addAccount(account4);

        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        accountRepository.save(account4);

        Celebrity celebrity1 =  Celebrity.of("BTS", "url1");
        Celebrity celebrity2 =  Celebrity.of("EXO", "url2");

        celebrityRepository.save(celebrity1);
        celebrityRepository.save(celebrity2);

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
        member1.addSaving(saving1);
        member1.addSaving(saving2);

        savingRepository.save(saving1);
        savingRepository.save(saving2);


        // 최애규칙
        SavingRule savingRule1 = SavingRule.of("울오빠 셀카", BigDecimal.valueOf(1_000), saving1);
        SavingRule savingRule2 = SavingRule.of("울오빠 라방", BigDecimal.valueOf(2_000), saving1);
        SavingRule savingRule3 = SavingRule.of("울오빠 스토리", BigDecimal.valueOf(1_500), saving1);

        savingRuleRepository.save(savingRule1);
        savingRuleRepository.save(savingRule2);
        savingRuleRepository.save(savingRule3);


        // 입금 내역
        SavingHistory savingHistory1 = SavingHistory.of(TransactionType.CREATION, BigDecimal.valueOf(10_000), saving1);
        SavingHistory savingHistory2 = SavingHistory.of(TransactionType.FREE, BigDecimal.valueOf(1_000), saving1);
        SavingHistory savingHistory3 = SavingHistory.of(TransactionType.FREE, BigDecimal.valueOf(2_000), saving1);
        SavingHistory savingHistory4 = SavingHistory.of(TransactionType.WEEK, BigDecimal.valueOf(10_000), saving1);

        savingHistoryRepository.save(savingHistory1);
        savingHistoryRepository.save(savingHistory2);
        savingHistoryRepository.save(savingHistory3);
        savingHistoryRepository.save(savingHistory4);


        /**
         * 콘서트
         */

        // 공연장
        ConcertVenue concertVenue1 = ConcertVenue.of("올림픽공원 우리금융아트홀", "서울특별시 송파구 올림픽로 424", 60_000);
        ConcertVenue concertVenue2 = ConcertVenue.of("상암디지털공연장", "서울특별시 마포구 상암산로 48-6", 1_000);

        concertVenueRepository.save(concertVenue1);
        concertVenueRepository.save(concertVenue2);

        // 공연
        Concert concert1 = Concert.of("2024 우리 원 더 스테이지", LocalDateTime.parse("2024-03-01T00:00:00"),
                LocalDateTime.parse("2024-04-30T23:59:59").withNano(999999),
                LocalDateTime.parse("2024-05-01T18:00:00"),
                LocalDateTime.parse("2024-05-02T19:00:00"),
                LocalDateTime.parse("2024-05-09T14:00:00"),
                120, "만12세 이상 관람가", "아이유\n RIIZE(라이즈)\n 르세라핌\n 성시경\n Special MC\n 김해준, 츄",
                "R석 - 우리카드 사용 고객 \n A석 : 최애 적금 가입 고객 B석 : 우리은행 원뱅크 가입 고객이면 누구나", "/img.png", concertVenue1);

        Concert concert2 = Concert.of("하나플레이리스트 콘서트", LocalDateTime.parse("2023-08-01T00:00:00"),
                LocalDateTime.parse("2023-08-30T23:59:59").withNano(999999),
                LocalDateTime.parse("2023-08-31T18:00:00"),
                LocalDateTime.parse("2023-09-01T14:00:00"),
                LocalDateTime.parse("2023-09-16T14:00:00"),
                120, "만12세 이상 관람가", "ZEROBASEONE, 멜로망스, 성시경, 싸이(PSY)",
                "하나원큐 앱 사용자", "/img.png", concertVenue2);


        concertRepository.save(concert1);
        concertRepository.save(concert1);

        // 좌석
        List<Seat> seats = new ArrayList<>();

        // 좌석 생성 및 List에 추가
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                String seatName = String.format("%d열 %d", i, j);
                SeatClass seatClass;

                if (i <= 3) {
                    seatClass = SeatClass.R;
                } else if (i <= 7) {
                    seatClass = SeatClass.A;
                } else {
                    seatClass = SeatClass.B;
                }

                Seat seat = Seat.of(seatClass, seatName, i, j, concertVenue1);
                seats.add(seat);
            }
        }

        // 일괄 저장
        for (Seat seat : seats) {
            seatRepository.save(seat);
        }

        ConcertHistory concertHistory1 = ConcertHistory.of(Status.APPLY, null, null, member2, null, concert1);
        ConcertHistory concertHistory2 = ConcertHistory.of(Status.APPLY, null, null, member2, null, concert1);
        ConcertHistory concertHistory3 = ConcertHistory.of(Status.APPLY, null, null, member3, null, concert1);
        ConcertHistory concertHistory4 = ConcertHistory.of(Status.APPLY, null, null, member4, null, concert1);
        ConcertHistory concertHistory5 = ConcertHistory.of(Status.APPLY, null, null, member5, null, concert1);

        concertHistoryRepository.save(concertHistory1);
        concertHistoryRepository.save(concertHistory2);
        concertHistoryRepository.save(concertHistory3);
        concertHistoryRepository.save(concertHistory4);
        concertHistoryRepository.save(concertHistory5);
    }
}