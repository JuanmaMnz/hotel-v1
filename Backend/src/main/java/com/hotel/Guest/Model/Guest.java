package com.hotel.Guest.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.Model.Id;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Guest {

    @EmbeddedId
    private Id guestId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JsonIgnore
    private Person person;

    @Column(nullable = false)
    private String address;
}