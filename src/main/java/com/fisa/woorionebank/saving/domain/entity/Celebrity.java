package com.fisa.woorionebank.saving.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "celebrity")
@Entity
public class Celebrity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "celebrity_id")
    private Long celebrityId;

    private String celebrityName;

    private String celebrityUrl;

    @Builder
    public Celebrity(
            String celebrityName,
            String celebrityUrl
    ) {
        this.celebrityName = celebrityName;
        this.celebrityUrl = celebrityUrl;
    }

    public static Celebrity of(
            String celebrityName,
            String celebrityUrl
    ) {
        return Celebrity.builder()
                .celebrityName(celebrityName)
                .celebrityUrl(celebrityUrl)
                .build();
    }


}
