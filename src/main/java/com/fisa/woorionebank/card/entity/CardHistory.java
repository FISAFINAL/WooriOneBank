package com.fisa.woorionebank.card.entity;

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
@Table(name = "card_history")
@Entity
public class CardHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_history_id")
    private Long cardHistoryId;

    private char BAS_YH;

    
    private char AGE;
}
