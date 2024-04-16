package com.fisa.woorionebank.seat.entity;

import com.fisa.woorionebank.concert.domain.entity.ConcertVenue;
import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "seat")
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private Long seatId;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;

    private String seatNumber;

    private int seatX;

    private int seatY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_venue_id")
    private ConcertVenue concertVenue;

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", seatClass=" + seatClass +
                ", seatNumber='" + seatNumber + '\'' +
                ", seatX=" + seatX +
                ", seatY=" + seatY +
                ", concertVenue=" + (concertVenue != null ? concertVenue.getConcertVenueId() : null) +
                '}';
    }

    public static Seat of(
            SeatClass seatClass,
            String seatNumber,
            int seatX,
            int seatY,
            ConcertVenue concertVenue
    ){
        return Seat.builder()
                .seatClass(seatClass)
                .seatNumber(seatNumber)
                .seatX(seatX)
                .seatY(seatY)
                .concertVenue(concertVenue)
                .build();
    }

}
