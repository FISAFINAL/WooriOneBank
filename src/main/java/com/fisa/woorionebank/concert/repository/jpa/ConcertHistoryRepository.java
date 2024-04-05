package com.fisa.woorionebank.concert.repository.jpa;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.repository.querydsl.ConcertHistoryRepositoryCustom;
import com.fisa.woorionebank.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

public interface ConcertHistoryRepository extends JpaRepository<ConcertHistory, Long>, ConcertHistoryRepositoryCustom {

    @Query("select m from ConcertHistory c join Member m on m.memberId = c.member.memberId where c.concert.concertId = :#{#concertId}")
    List<Member> findMemberByConcertId(Long concertId);

//    @Query("select u from Following f join Users u on u.userSeq = f.user.userSeq where f.user.userSeq = :#{#user.userSeq}")
//    List<Users> findProfileThatUserFollows(@Param("user") Users user);
}
