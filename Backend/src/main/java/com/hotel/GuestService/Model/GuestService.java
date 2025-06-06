package com.hotel.GuestService.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotel.Guest.Model.Guest;
import com.hotel.HotelServices.Model.HotelServices;
import com.hotel.Invoice.Model.Invoice;
import com.hotel.Reservation.Model.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest_service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guestServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private HotelServices service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "guest_id_type", referencedColumnName = "person_identity_document_type"),
            @JoinColumn(name = "guest_id_number", referencedColumnName = "person_identity_document_number")
    })
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    @JsonBackReference
    private Reservation reservation;

    @Column(name = "usage_date", nullable = false)
    private LocalDateTime usageDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;
}