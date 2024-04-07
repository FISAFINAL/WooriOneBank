package com.fisa.woorionebank.concert.repository.jpa;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.repository.querydsl.ConcertRepositoryCustom;
import com.fisa.woorionebank.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long>, ConcertRepositoryCustom {

    @Query("select m from ConcertHistory c join Member m on m.memberId = c.member.memberId where c.concert.concertId = :#{#concertId}")
    List<Member> findMemberByConcertId(Long concertId);

    @Query("select c.concertVenue.concertVenueId from Concert c where c.concertVenue.concertVenueId = :#{#concertId}")
    Long findConcertVenueByConcertId(Long concertId);
}
