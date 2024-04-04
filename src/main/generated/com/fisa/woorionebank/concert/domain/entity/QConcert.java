package com.fisa.woorionebank.concert.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConcert is a Querydsl query type for Concert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConcert extends EntityPathBase<Concert> {

    private static final long serialVersionUID = 1142141523L;

    public static final QConcert concert = new QConcert("concert");

    public final StringPath ageLimit = createString("ageLimit");

    public final DateTimePath<java.time.LocalDateTime> checkDate = createDateTime("checkDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> concertDate = createDateTime("concertDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> concertId = createNumber("concertId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final StringPath location = createString("location");

    public final NumberPath<Integer> runningTime = createNumber("runningTime", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> ticketingDate = createDateTime("ticketingDate", java.time.LocalDateTime.class);

    public QConcert(String variable) {
        super(Concert.class, forVariable(variable));
    }

    public QConcert(Path<? extends Concert> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConcert(PathMetadata metadata) {
        super(Concert.class, metadata);
    }

}

