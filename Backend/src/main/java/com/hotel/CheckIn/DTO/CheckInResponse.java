package com.hotel.CheckIn.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.identity_document.Model.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CheckInResponse(

        Integer checkInId,

        Id guestId,

        Id employeeId,

        Integer reservationId,

        boolean hasCompanion,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Bogota")
        LocalDateTime checkInDate

) implements Serializable {
}
