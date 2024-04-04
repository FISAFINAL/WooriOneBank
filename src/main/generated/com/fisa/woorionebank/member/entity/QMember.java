package com.fisa.woorionebank.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 2053387355L;

    public static final QMember member = new QMember("member1");

    public final com.fisa.woorionebank.common.QBaseEntity _super = new com.fisa.woorionebank.common.QBaseEntity(this);

    public final ListPath<com.fisa.woorionebank.account.entity.Account, com.fisa.woorionebank.account.entity.QAccount> accounts = this.<com.fisa.woorionebank.account.entity.Account, com.fisa.woorionebank.account.entity.QAccount>createList("accounts", com.fisa.woorionebank.account.entity.Account.class, com.fisa.woorionebank.account.entity.QAccount.class, PathInits.DIRECT2);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath email = createString("email");

    public final EnumPath<Grade> grade = createEnum("grade", Grade.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath loginId = createString("loginId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedTime = _super.modifiedTime;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<com.fisa.woorionebank.saving.entity.Saving, com.fisa.woorionebank.saving.entity.QSaving> savings = this.<com.fisa.woorionebank.saving.entity.Saving, com.fisa.woorionebank.saving.entity.QSaving>createList("savings", com.fisa.woorionebank.saving.entity.Saving.class, com.fisa.woorionebank.saving.entity.QSaving.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

