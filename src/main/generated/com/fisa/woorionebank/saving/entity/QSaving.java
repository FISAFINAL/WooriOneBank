package com.fisa.woorionebank.saving.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSaving is a Querydsl query type for Saving
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaving extends EntityPathBase<Saving> {

    private static final long serialVersionUID = -351806181L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSaving saving = new QSaving("saving");

    public final StringPath account = createString("account");

    public final QCelebrity celebrity;

    public final EnumPath<DepositDay> depositDay = createEnum("depositDay", DepositDay.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final StringPath linkedAccount = createString("linkedAccount");

    public final com.fisa.woorionebank.member.entity.QMember member;

    public final NumberPath<Integer> overdueWeek = createNumber("overdueWeek", Integer.class);

    public final NumberPath<Long> savingId = createNumber("savingId", Long.class);

    public final StringPath savingName = createString("savingName");

    public final ListPath<SavingRule, QSavingRule> savingRules = this.<SavingRule, QSavingRule>createList("savingRules", SavingRule.class, QSavingRule.class, PathInits.DIRECT2);

    public final ListPath<com.fisa.woorionebank.savinghistory.entity.SavingHistory, com.fisa.woorionebank.savinghistory.entity.QSavingHistory> savings = this.<com.fisa.woorionebank.savinghistory.entity.SavingHistory, com.fisa.woorionebank.savinghistory.entity.QSavingHistory>createList("savings", com.fisa.woorionebank.savinghistory.entity.SavingHistory.class, com.fisa.woorionebank.savinghistory.entity.QSavingHistory.class, PathInits.DIRECT2);

    public final StringPath savingUrl = createString("savingUrl");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public QSaving(String variable) {
        this(Saving.class, forVariable(variable), INITS);
    }

    public QSaving(Path<? extends Saving> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSaving(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSaving(PathMetadata metadata, PathInits inits) {
        this(Saving.class, metadata, inits);
    }

    public QSaving(Class<? extends Saving> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.celebrity = inits.isInitialized("celebrity") ? new QCelebrity(forProperty("celebrity"), inits.get("celebrity")) : null;
        this.member = inits.isInitialized("member") ? new com.fisa.woorionebank.member.entity.QMember(forProperty("member")) : null;
    }

}

