package com.fisa.woorionebank.member.repository;

import com.fisa.woorionebank.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);

    Boolean existsByEmail(String email);
}
