package com.fisa.woorionebank.saving.repository.saving;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SavingRepository extends JpaRepository<Saving,Long>, SavingRepositoryCustom {

    //간단한 쿼리 작성
    Optional<Saving> findBySavingAccount(String savingAccount);

    @Query("SELECT s.savingId FROM Saving s WHERE s.member.memberId = ?1")
    Optional<Long> findByMemberId(Long memberId);

    //복잡한 쿼리 이미 상속을 받았기 때문에 쓸필요 없다 @!!!!!

}
