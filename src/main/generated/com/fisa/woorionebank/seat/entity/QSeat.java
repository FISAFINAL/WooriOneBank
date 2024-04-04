package com.fisa.woorionebank.seat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeat is a Querydsl query type for Seat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeat extends EntityPathBase<Seat> {

    private static final long serialVersionUID = -913905295L;

    public static final QSeat seat = new QSeat("seat");

    public final EnumPath<SeatClass> seatClass = createEnum("seatClass", SeatClass.class);

    public final NumberPath<Long> seatId = createNumber("seatId", Long.class);

    public final StringPath seatNumber = createString("seatNumber");

    public final NumberPath<Integer> seatX = createNumber("seatX", Integer.class);

    public final NumberPath<Integer> seatY = createNumber("seatY", Integer.class);

    public QSeat(String variable) {
        super(Seat.class, forVariable(variable));
    }

    public QSeat(Path<? extends Seat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeat(PathMetadata metadata) {
        super(Seat.class, metadata);
    }

}

