package com.fisa.woorionebank.config.redisson;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * AOP에서 트랜잭션 분리를 위한 클래스
 */
@Component
public class AopForTransaction {

    // 어노테이션은 이 메서드가 트랜잭션을 분리해서 실행하도록 지정합니다.
    // 이것은 메서드가 호출될 때마다 새로운 트랜잭션을 시작하고, 이전 트랜잭션과 독립적으로 실행됨을 의미합니다.
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 해당 AOP가 동작할 때 새롭게 트랜잭션을 시작
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
