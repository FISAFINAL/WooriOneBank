package com.fisa.woorionebank.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '생성일자'")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정일자'")
    private LocalDateTime modifiedTime;

    @Column(columnDefinition = "bit default false NULL COMMENT '이용가능여부'")
    private Boolean isDeleted;

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
