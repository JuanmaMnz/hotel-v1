package com.hotel.Invoice.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hotel.Guest.Model.Guest;
import com.hotel.GuestService.Model.GuestService;
import com.hotel.Reservation.Model.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "guest_id_type", referencedColumnName = "person_identity_document_type"),
            @JoinColumn(name = "guest_id_number", referencedColumnName = "person_identity_document_number")
    })
    private Guest guest;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GuestService> usedServices;

    @Column(name = "total_cost_of_used_services", nullable = false)
    BigDecimal totalCostOfUsedServices;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
}