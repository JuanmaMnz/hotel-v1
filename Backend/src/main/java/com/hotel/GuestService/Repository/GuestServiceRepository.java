package com.hotel.GuestService.Repository;

import com.hotel.GuestService.Model.GuestService;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GuestServiceRepository extends JpaRepository<GuestService, Integer> {

    @Query("""
    SELECT gs
    FROM GuestService gs
    WHERE gs.guest.guestId = :guestId
    AND gs.reservation.reservationId = :reservationId
    AND gs.usageDate BETWEEN :checkInDate AND :checkOutDate
    """)
    Page<GuestService> findServicesUsedByGuestDuringStay(@Param("guestId") Id guestId,
                                                         @Param("reservationId") Integer reservationId,
                                                         @Param("checkInDate") LocalDateTime checkInDate,
                                                         @Param("checkOutDate") LocalDateTime checkOutDate,
                                                         Pageable pageable);

    @Query("""
    SELECT gs
    FROM GuestService gs
    WHERE gs.guest.guestId = :guestId
    AND gs.reservation.reservationId = :reservationId
    AND gs.usageDate BETWEEN :checkInDate AND :checkOutDate
    """)
    List<GuestService> findServicesUsedByGuestDuringStay(@Param("guestId") Id guestId,
                                                         @Param("reservationId") Integer reservationId,
                                                         @Param("checkInDate") LocalDateTime checkInDate,
                                                         @Param("checkOutDate") LocalDateTime checkOutDate);
}