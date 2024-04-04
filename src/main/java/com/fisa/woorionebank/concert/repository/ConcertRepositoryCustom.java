package com.fisa.woorionebank.concert.repository;

import com.fisa.woorionebank.concert.domain.entity.Concert;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ConcertRepositoryCustom extends QuerydslPredicateExecutor<Concert> {
}
