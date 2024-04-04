package com.fisa.woorionebank.saving.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCelebrity is a Querydsl query type for Celebrity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCelebrity extends EntityPathBase<Celebrity> {

    private static final long serialVersionUID = 182769698L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCelebrity celebrity = new QCelebrity("celebrity");

    public final NumberPath<Long> celebrityId = createNumber("celebrityId", Long.class);

    public final StringPath celebrityName = createString("celebrityName");

    public final StringPath celebrityUrl = createString("celebrityUrl");

    public final QSaving saving;

    public QCelebrity(String variable) {
        this(Celebrity.class, forVariable(variable), INITS);
    }

    public QCelebrity(Path<? extends Celebrity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCelebrity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCelebrity(PathMetadata metadata, PathInits inits) {
        this(Celebrity.class, metadata, inits);
    }

    public QCelebrity(Class<? extends Celebrity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.saving = inits.isInitialized("saving") ? new QSaving(forProperty("saving"), inits.get("saving")) : null;
    }

}

