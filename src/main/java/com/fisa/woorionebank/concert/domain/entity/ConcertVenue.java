package com.fisa.woorionebank.concert.domain.entity;

import com.fisa.woorionebank.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "concert_venue")
@Entity
public class ConcertVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "venue_id")
    private Long venueId;

    private String venueName;

    private String venueAddress;

    private int capacity;

    @OneToMany(mappedBy = "concert_venue")
    private List<Concert> concerts = new ArrayList<>();

    @OneToMany(mappedBy = "concert_venue")
    private List<Seat> seats = new ArrayList<>();

}
