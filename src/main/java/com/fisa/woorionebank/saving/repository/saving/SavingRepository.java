package com.fisa.woorionebank.saving.repository.saving;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingRepository extends JpaRepository<Saving,Long>, SavingRepositoryCustom {

    //간단한 쿼리 작성
    Optional<Saving> findBySavingAccount(String savingAccount);

    //복잡한 쿼리 이미 상속을 받았기 때문에 쓸필요 없다 @!!!!!

}
