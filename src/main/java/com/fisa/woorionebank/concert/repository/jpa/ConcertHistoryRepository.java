package com.fisa.woorionebank.concert.repository.jpa;

import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.querydsl.ConcertHistoryRepositoryCustom;
import com.fisa.woorionebank.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ConcertHistoryRepository extends JpaRepository<ConcertHistory, Long>, ConcertHistoryRepositoryCustom {

    @Query("select m from ConcertHistory c join Member m on m.memberId = c.member.memberId where c.concert.concertId = :#{#concertId}")
    List<Member> findMemberByConcertId(Long concertId);

    @Query("select c from ConcertHistory c where c.member.memberId = ?1 and c.concert.concertId = ?2")
    Optional<ConcertHistory> findByMemberIdAndConcertId(Long memberId, Long concertId);

    @Query("SELECT c FROM ConcertHistory c JOIN FETCH c.concert WHERE c.status = :status and c.concert.concertId = :concertId")
    List<ConcertHistory> findByStatusAndConcertId(Status status, Long concertId);

}
