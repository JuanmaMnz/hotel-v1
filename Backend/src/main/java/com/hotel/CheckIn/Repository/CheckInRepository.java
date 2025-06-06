package com.hotel.CheckIn.Repository;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {

    Page<CheckIn> findAllByCheckInDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query("""
    SELECT COUNT(c) > 0
    FROM CheckIn c
    WHERE c.guest.guestId = :guestId
      AND c.reservation.checkOutDate > CURRENT_TIMESTAMP
    """)
    boolean hasCompletedCheckIn(@Param("guestId") Id guestId);

    boolean existsByReservation(Reservation reservation);

    Optional<CheckIn> findByReservation(Reservation reservation);
}