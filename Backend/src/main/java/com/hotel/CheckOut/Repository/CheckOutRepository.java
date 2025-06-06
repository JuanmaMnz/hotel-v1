package com.hotel.CheckOut.Repository;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckOut.Model.CheckOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Integer> {

    @Query("SELECT c FROM CheckOut c WHERE c.checkOutDate BETWEEN :start AND :end")
    Page<CheckOut> findByCheckOutDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("""
    SELECT CASE
            WHEN COUNT(c) > 0 THEN true
            ELSE false
        END
    FROM CheckOut c
    WHERE c.checkIn = :checkIn
    """)
    boolean existsByCheckIn(@Param("checkIn") CheckIn checkIn);

    @Query("""
    SELECT CASE
           WHEN COUNT(c) > 0 THEN true
           ELSE false
           END
    FROM CheckOut c
    JOIN c.checkIn ci
    WHERE ci.reservation.reservationId = :reservationId
    """)
    boolean existsByReservationId(@Param("reservationId") Integer reservationId);

}