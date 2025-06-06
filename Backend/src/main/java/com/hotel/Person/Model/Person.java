package com.hotel.Person.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.Employee.Model.Employee;
import com.hotel.Guest.Model.Guest;
import com.hotel.Person.Enums.Gender;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "person")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    @EmbeddedId
    private Id personId;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @MapsId
    @JsonIgnore
    private IdentityDocument identityDocument;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING, timezone = "america/bogota")
    @Column(name = "birth_of_date", nullable = false)
    private LocalDate birthOfDate;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    private Employee employee;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    private Guest guest;

}