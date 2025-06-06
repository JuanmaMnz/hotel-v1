package com.hotel.CheckOut.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.Employee.Model.Employee;
import com.hotel.Guest.Model.Guest;
import com.hotel.Invoice.Model.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_out")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_out_id")
    private Integer checkOutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "guest_id_type"),
            @JoinColumn(name = "guest_id_number")
    })
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "employee_id_type"),
            @JoinColumn(name = "employee_id_number")
    })
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_in_id")
    private CheckIn checkIn;

    @Column(name = "check_out_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING, timezone = "America/Bogota")
    private LocalDateTime checkOutDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}