package com.hotel.identity_document.Model;

import com.hotel.identity_document.Enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class Id implements Serializable {

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "type", nullable = false, updatable = false)
    private Type type;

    @Column(name = "number", nullable = false, updatable = false)
    private String number;
}