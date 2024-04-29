package com.fisa.woorionebank.saving.repository.celebrity;

import com.fisa.woorionebank.saving.domain.entity.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {

}
