package com.hotel.Person.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Person.Enums.Gender;
import com.hotel.identity_document.Model.IdentityDocument;

import java.io.Serializable;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PersonResponse(

        IdentityDocument identityDocument,

        String firstName,

        String lastName,

        String phoneNumber,

        Gender gender,

        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING, timezone = "america/bogota")
        LocalDate birthOfDate

) implements Serializable {
}
