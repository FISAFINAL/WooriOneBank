package com.fisa.woorionebank.concert.domain.entity;

import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
@Table(name = "concert_venue")
@Entity
public class ConcertVenue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "concert_venue_id")
    private Long concertVenueId;

    private String venueName;

    private String venueAddress;

    private int capacity;

    @Builder.Default
    @OneToMany(mappedBy = "concertVenue")
    private List<Concert> concerts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "concertVenue")
    private List<Seat> seats = new ArrayList<>();

    public ConcertVenue(Long concertVenueId, String venueName, String venueAddress, int capacity) {
        this.concertVenueId = concertVenueId;
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.capacity = capacity;
    }

    public static ConcertVenue of(
            String venueName,
            String venueAddress,
            int capacity,
            List<Concert> concerts,
            List<Seat> seats
    ){
        return ConcertVenue.builder()
                .venueName(venueName)
                .venueAddress(venueAddress)
                .capacity(capacity)
                .concerts(concerts)
                .seats(seats)
                .build();
    }
}
