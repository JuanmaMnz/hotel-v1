package com.hotel.Invoice.Repository;

import com.hotel.Invoice.Model.Invoice;
import com.hotel.Reservation.Model.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    boolean existsByReservation(Reservation reservation);

    @EntityGraph(attributePaths = {"usedServices"})
    Optional<Invoice> findById(Integer invoiceId);
}