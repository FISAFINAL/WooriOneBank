package com.fisa.woorionebank.saving.repository;

import com.fisa.woorionebank.saving.domain.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingRepository extends JpaRepository<Saving,Long>, SavingRepositoryCustom {

    //간단한 쿼리 작성

    //복잡한 쿼리 이미 상속을 받았기 때문에 쓸필요 없다 @!!!!!

}
