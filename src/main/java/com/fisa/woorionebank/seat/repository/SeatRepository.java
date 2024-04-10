package com.fisa.woorionebank.seat.repository;

import com.fisa.woorionebank.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findById(Long seatId);
    @Query("select s.seatId from Seat s where s.concertVenue.concertVenueId = :#{#concertVenueId}")
    List<Long> findSeatByConcertVenueId(Long concertVenueId);

    @Query("select s from Seat s where s.seatX = 1 and s.seatY = 1")
    Optional<Seat> findSeatIdBySeatXAndSeatY(int seatx, int seaty);
}
