package com.fisa.woorionebank.concert.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "concert")
@Entity
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "concert_id")
    private Long concertId;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime checkDate;

    private LocalDateTime ticketingDate;

    private LocalDateTime concertDate;

    private int runningTime;

    private String ageLimit;
}
