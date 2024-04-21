package com.fisa.woorionebank.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.ConcertVenue;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.repository.jpa.ConcertRepository;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.service.SavingService;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.entity.Seat;
import com.fisa.woorionebank.seat.entity.SeatClass;
import com.fisa.woorionebank.seat.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class) // 스프링이랑 JUnit 5를 통합해서 스프링 기능을 테스트에 사용가능 하게 하는 어노테이션!
@SpringBootTest
@Transactional
public class DistributedLockTest {
    @Autowired
    EntityManager em;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private ConcertHistoryRepository concertHistoryRepository;

    @Test
    @Rollback(value = false)
    public void 테스트데이터삽입() throws Exception {

        ConcertVenue concertVenue = ConcertVenue.of("킨텍스", "일산", 1_000);
        em.persist(concertVenue);
        Seat seat = Seat.of(SeatClass.A, "10열 11", 10, 11, concertVenue);
        em.persist(seat);
        Concert concert = Concert.of("2024 우리 원 더 스테이지", LocalDateTime.parse("2024-03-01T00:00:00"),
                LocalDateTime.parse("2024-04-30T23:59:59").withNano(999999),
                LocalDateTime.parse("2024-05-01T18:00:00"),
                LocalDateTime.parse("2024-05-02T19:00:00"),
                LocalDateTime.parse("2024-05-09T14:00:00"),
                120, "만12세 이상 관람가", "아이유\n RIIZE(라이즈)\n 르세라핌\n 성시경\n Special MC\n 김해준, 츄",
                "R석 - 우리카드 사용 고객 \n A석 : 최애 적금 가입 고객 B석 : 우리은행 원뱅크 가입 고객이면 누구나", "/img.png", concertVenue);
        em.persist(concert);

        for (int i = 0; i < 100; i++) {
            Member member = new Member("ID" + i, "PW" + i, "member" + i, 24, "email" + i, Grade.NONE);
            em.persist(member);
            ConcertHistory concertHistory = ConcertHistory.of(Status.APPLY, null, null, member, null, concert);
            em.persist(concertHistory);

        }


    }


    @Test
    public void 티켓팅_100명_LockX() throws Exception{

        List<Member> memberList = memberRepository.findAll();
        Seat seat = seatRepository.findSeat(10, 11)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Seat));
        Concert concert = concertRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        System.out.println(memberList.size());
        System.out.println(seat.getSeatNumber());

        RequestSeatDTO dto = new RequestSeatDTO(concert.getConcertId(),10,11);

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Member member = memberList.get(0);
        System.out.println(member.getName());
        for (int i = 0; i < numberOfThreads; i++) {
            int index = i;
            executorService.submit(() -> {
                try {
                    System.out.println(memberList.get(index).getName() + " 멤버 요청");
                    concertService.reserveSeat(memberList.get(index), dto);
                    System.out.println(memberList.get(index).getName() + " 멤버 요청 끝@@@");

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<ConcertHistory> bySeat = concertHistoryRepository.findBySeat(seat);

        assertThat(bySeat.size()).isEqualTo(1);
    }

    @Test
    public void 티켓팅_100명_분산Lock() throws Exception{

        List<Member> memberList = memberRepository.findAll();
        Seat seat = seatRepository.findSeat(10, 11)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Seat));
        Concert concert = concertRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Concert));

        System.out.println(memberList.size());
        System.out.println(seat.getSeatNumber());

        RequestSeatDTO dto = new RequestSeatDTO(concert.getConcertId(),10,11);

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Member member = memberList.get(0);
        System.out.println(member.getName());
        for (int i = 0; i < numberOfThreads; i++) {
            int index = i;
            executorService.submit(() -> {
                try {
                    System.out.println(memberList.get(index).getName() + " 멤버 요청");
                    concertService.reserveSeat2(seat.getSeatNumber(), memberList.get(index), dto);
                    System.out.println(memberList.get(index).getName() + " 멤버 요청 끝@@@");

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<ConcertHistory> bySeat = concertHistoryRepository.findBySeat(seat);

        assertThat(bySeat.size()).isEqualTo(1);
    }


}

