package com.fisa.woorionebank.concert.repository.querydsl;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ConcertRepositoryCustom extends QuerydslPredicateExecutor<Concert> {
}
