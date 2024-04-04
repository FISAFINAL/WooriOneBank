package com.fisa.woorionebank.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCardHistory is a Querydsl query type for CardHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardHistory extends EntityPathBase<CardHistory> {

    private static final long serialVersionUID = -732935283L;

    public static final QCardHistory cardHistory = new QCardHistory("cardHistory");

    public final ComparablePath<Character> AGE = createComparable("AGE", Character.class);

    public final ComparablePath<Character> BAS_YH = createComparable("BAS_YH", Character.class);

    public final NumberPath<Long> cardHistoryId = createNumber("cardHistoryId", Long.class);

    public QCardHistory(String variable) {
        super(CardHistory.class, forVariable(variable));
    }

    public QCardHistory(Path<? extends CardHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCardHistory(PathMetadata metadata) {
        super(CardHistory.class, metadata);
    }

}

