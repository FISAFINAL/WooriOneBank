package com.fisa.woorionebank.savinghistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavingHistory is a Querydsl query type for SavingHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavingHistory extends EntityPathBase<SavingHistory> {

    private static final long serialVersionUID = -644940179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavingHistory savingHistory = new QSavingHistory("savingHistory");

    public final com.fisa.woorionebank.account.entity.QAccount account;

    public final com.fisa.woorionebank.saving.entity.QSaving saving;

    public final NumberPath<Long> savingHistoryId = createNumber("savingHistoryId", Long.class);

    public final EnumPath<TransactionType> transactionType = createEnum("transactionType", TransactionType.class);

    public QSavingHistory(String variable) {
        this(SavingHistory.class, forVariable(variable), INITS);
    }

    public QSavingHistory(Path<? extends SavingHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavingHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavingHistory(PathMetadata metadata, PathInits inits) {
        this(SavingHistory.class, metadata, inits);
    }

    public QSavingHistory(Class<? extends SavingHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.fisa.woorionebank.account.entity.QAccount(forProperty("account"), inits.get("account")) : null;
        this.saving = inits.isInitialized("saving") ? new com.fisa.woorionebank.saving.entity.QSaving(forProperty("saving"), inits.get("saving")) : null;
    }

}

