package com.fisa.woorionebank.saving.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavingRule is a Querydsl query type for SavingRule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavingRule extends EntityPathBase<SavingRule> {

    private static final long serialVersionUID = 997515959L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavingRule savingRule = new QSavingRule("savingRule");

    public final NumberPath<Integer> depositAmount = createNumber("depositAmount", Integer.class);

    public final QSaving saving;

    public final NumberPath<Long> savingRuleId = createNumber("savingRuleId", Long.class);

    public final StringPath savingRuleName = createString("savingRuleName");

    public QSavingRule(String variable) {
        this(SavingRule.class, forVariable(variable), INITS);
    }

    public QSavingRule(Path<? extends SavingRule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavingRule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavingRule(PathMetadata metadata, PathInits inits) {
        this(SavingRule.class, metadata, inits);
    }

    public QSavingRule(Class<? extends SavingRule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.saving = inits.isInitialized("saving") ? new QSaving(forProperty("saving"), inits.get("saving")) : null;
    }

}

