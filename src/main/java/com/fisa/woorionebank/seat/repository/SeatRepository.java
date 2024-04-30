package com.fisa.woorionebank.seat.repository;

import com.fisa.woorionebank.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findById(Long seatId);

    @Query("select s.seatId from Seat s where s.concertVenue.concertVenueId = :concertVenueId")
    List<Long> findSeatByConcertVenueId(@Param("concertVenueId") Long concertVenueId);

    @Query("select s from Seat s where s.seatX = :seatX and s.seatY = :seatY")
    Optional<Seat> findSeatIdBySeatXAndSeatY(@Param("seatX") int seatX, @Param("seatY") int seatY);

    @Query("select s from Seat s where s.seatX = :seatX and s.seatY = :seatY")
    Optional<Seat> findSeat(@Param("seatX") int seatX, @Param("seatY") int seatY);
}
