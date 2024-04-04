package com.fisa.woorionebank.concert.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConcertHistory is a Querydsl query type for ConcertHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConcertHistory extends EntityPathBase<ConcertHistory> {

    private static final long serialVersionUID = 1782191809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConcertHistory concertHistory = new QConcertHistory("concertHistory");

    public final EnumPath<Area> area = createEnum("area", Area.class);

    public final QConcert concert;

    public final NumberPath<Long> concertReservationId = createNumber("concertReservationId", Long.class);

    public final com.fisa.woorionebank.member.entity.QMember member;

    public final com.fisa.woorionebank.seat.entity.QSeat seat;

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final DateTimePath<java.time.LocalDateTime> ticketingDate = createDateTime("ticketingDate", java.time.LocalDateTime.class);

    public QConcertHistory(String variable) {
        this(ConcertHistory.class, forVariable(variable), INITS);
    }

    public QConcertHistory(Path<? extends ConcertHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConcertHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConcertHistory(PathMetadata metadata, PathInits inits) {
        this(ConcertHistory.class, metadata, inits);
    }

    public QConcertHistory(Class<? extends ConcertHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.concert = inits.isInitialized("concert") ? new QConcert(forProperty("concert")) : null;
        this.member = inits.isInitialized("member") ? new com.fisa.woorionebank.member.entity.QMember(forProperty("member")) : null;
        this.seat = inits.isInitialized("seat") ? new com.fisa.woorionebank.seat.entity.QSeat(forProperty("seat")) : null;
    }

}

