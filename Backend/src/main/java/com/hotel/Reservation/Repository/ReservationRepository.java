package com.hotel.Reservation.Repository;

import com.hotel.Guest.Model.Guest;
import com.hotel.Reservation.Enums.ReservationStatus;
import com.hotel.Reservation.Model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
    SELECT COUNT(r) FROM Reservation r
    WHERE r.room.id = :roomId
    AND r.reservationStatus <> 'CANCELADA'
    AND (
        (:checkIn BETWEEN r.checkInDate AND r.checkOutDate)
        OR (:checkOut BETWEEN r.checkInDate AND r.checkOutDate)
        OR (r.checkInDate BETWEEN :checkIn AND :checkOut)
    )
    """)
    int countOverlappingReservations(@Param("roomId") Integer roomId,
                                     @Param("checkIn") LocalDateTime checkIn,
                                     @Param("checkOut") LocalDateTime checkOut);

    @Query("""
    SELECT r.room.roomId
    FROM Reservation r
    WHERE r.reservationStatus <> 'CANCELADA'
    AND (
        (r.checkInDate < :checkOutDate AND r.checkOutDate > :checkInDate)
    )
    """)
    List<Integer> findRoomIdsWithReservationsBetween(@Param("checkInDate") LocalDateTime checkInDate,
                                                     @Param("checkOutDate") LocalDateTime checkOutDate);

    Optional<Reservation> findTopByGuestAndReservationStatusOrderByReservationIdDesc(Guest guest, ReservationStatus reservationStatus);

    Page<Reservation> findByRoomRoomIdAndCheckInDateBetween(
            Integer roomId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(r) FROM Reservation r
    WHERE r.guest = :guest
    AND r.reservationStatus <> 'CANCELADA'
    """)
    int countNonCancelledReservationsByGuest(@Param("guest") Guest guest);

}