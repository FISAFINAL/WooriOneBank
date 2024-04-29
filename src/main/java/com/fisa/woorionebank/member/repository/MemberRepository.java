package com.fisa.woorionebank.member.repository;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
//    findByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);

    Boolean existsByEmail(String email);

}
