package com.fisa.woorionebank.interest.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInterestRate is a Querydsl query type for InterestRate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterestRate extends EntityPathBase<InterestRate> {

    private static final long serialVersionUID = -626970053L;

    public static final QInterestRate interestRate = new QInterestRate("interestRate");

    public final NumberPath<Long> interestId = createNumber("interestId", Long.class);

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    public QInterestRate(String variable) {
        super(InterestRate.class, forVariable(variable));
    }

    public QInterestRate(Path<? extends InterestRate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInterestRate(PathMetadata metadata) {
        super(InterestRate.class, metadata);
    }

}

