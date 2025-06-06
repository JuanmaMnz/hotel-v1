package com.hotel.identity_document.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "identity_document")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class IdentityDocument {

    @EmbeddedId
    private Id id;

    @Column(name = "issuing_country", nullable = false)
    private String issuingCountry;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING, timezone = "america/bogota")
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
}