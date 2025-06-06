package com.hotel.CheckIn.Model;

import com.hotel.Employee.Model.Employee;
import com.hotel.Guest.Model.Guest;
import com.hotel.Reservation.Model.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_in_id")
    private Integer checkInId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "guest_id_type"),
            @JoinColumn(name = "guest_id_number")
    })
    private Guest guest;

    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "employee_id_type"),
            @JoinColumn(name = "employee_id_number")
    })
    private Employee employee;

    @Column(name = "has_companion", nullable = false)
    private boolean hasCompanion;

    @Column(name = "check_in_date", nullable = false)
    private LocalDateTime checkInDate;
}