package com.fisa.woorionebank.service;


import com.fisa.woorionebank.concert.domain.dto.ConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.RequestDrawDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseConcertDTO;
import com.fisa.woorionebank.concert.domain.dto.ResponseDrawDTO;
import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.ConcertVenue;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.concert.service.ConcertDrawService;
import com.fisa.woorionebank.concert.service.ConcertService;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.seat.domain.dto.RequestSeatDTO;
import com.fisa.woorionebank.seat.domain.dto.SeatListDTO;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ConcertServiceTest {
    @Autowired
    private ConcertHistoryRepository concertHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private ConcertDrawService concertDrawService;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 공연조회() throws Exception {
        // given
        ConcertVenue concertVenue = new ConcertVenue(2L, "올림픽공원 우리금융아트홀", "서울특별시 송파구 올림픽로 424", 60000);
        em.merge(concertVenue);

        ConcertDTO concertDTO = ConcertDTO.builder()
                .concertName("2024 우리 원 더 스테이지")
                .startDate(LocalDateTime.parse("2024-03-01T00:00:00"))
                .endDate(LocalDateTime.parse("2024-04-30T23:59:59").withNano(999999))
                .checkDate(LocalDateTime.parse("2024-05-01T18:00:00"))
                .ticketingDate(LocalDateTime.parse("2024-05-02T14:00:00"))
                .concertDate(LocalDateTime.parse("2024-05-09T14:00:00"))
                .runningTime(120)
                .ageLimit("만12세 이상 관람가")
                .lineup("아이유\n RIIZE(라이즈)\n 르세라핌\n 성시경\n Special MC\n 김해준, 츄")
                .drawInfo("R석 - 우리카드 사용 고객 \n A석 : 최애 적금 가입 고객 B석 : 우리은행 원뱅크 가입 고객이면 누구나")
                .imgUrl("/img.png")
                .build();
        Concert concert = new Concert();
        concert.createConcert(concertDTO);
        em.persist(concert);

        // when
        ResponseConcertDTO responseConcertDTO = concertService.searchConcert(1L);

        // then
        assertThat(responseConcertDTO.getConcertName()).isEqualTo("2024 우리 원 더 스테이지");
    }

    @Test
    public void 공연응모() throws Exception {
        // given
        Member member = new Member("ID1", "PW1", "박예린", 25, "email1@naver.com", Grade.VVIP);
        em.persist(member);

        RequestDrawDTO requestDrawDTO = RequestDrawDTO.builder()
                .concertId(1L)
                .build();

        // when
        concertService.applyConcert(member, requestDrawDTO.getConcertId());

        // then
        Optional<ConcertHistory> concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(1L, 1L);
        assertThat(concertHistory.get().getStatus()).isEqualTo(Status.APPLY);
    }

    @Test
    public void 공연응모당첨() throws Exception {
        // when
        concertDrawService.drawConcert(1L);

        int now = LocalDateTime.now().getDayOfMonth();

        // then
        List<ConcertHistory> concertHistories = concertHistoryRepository.findByStatusAndConcertId(Status.WIN, 1L);
        assertThat(concertHistories.get(0).getTicketingDate().getDayOfMonth()).isEqualTo(now);
    }

    @Test
    public void 공연당첨조회() throws Exception {
        // given
//        Member member = new Member("ID1", "PW1", "박예린", 25, "email1@naver.com", Grade.VVIP);
//        em.merge(member);
        Member member = memberRepository.findById(1L).orElse(null);

        // when
        ResponseDrawDTO responseDrawDTO = concertService.searchDrawResult(member, 1L);

        // then
        assertThat(responseDrawDTO.getConcertName()).isEqualTo("2024 우리 원 더 스테이지");
    }

    @Test
    public void 공연좌석조회() throws Exception {
        // when
        SeatListDTO seats = concertService.selectSeat(1L);

        // then
        assertThat(seats.getResponseSeatDTOList().isEmpty()).isEqualTo(false);
    }

    @Test
    public void 공연좌석예매() throws Exception {
        // given
//        Member member = new Member("ID1", "PW1", "박예린", 25, "email1@naver.com", Grade.VVIP);
//        em.merge(member);
        Member member = memberRepository.findById(1L).orElse(null);

        RequestSeatDTO requestSeatDTO = RequestSeatDTO.builder()
                .concertId(1L)
                .seatX(1)
                .seatY(1)
                .build();

        // when
        concertService.reserveSeat(member, requestSeatDTO);

        // then
        ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(member.getMemberId(), requestSeatDTO.getConcertId()).orElse(null);
        assertThat(concertHistory.getStatus()).isEqualTo(Status.SUCCESS);
    }
}