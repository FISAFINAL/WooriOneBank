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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_id")
    private Saving saving;
}
