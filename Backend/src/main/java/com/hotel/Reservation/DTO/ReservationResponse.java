package com.hotel.Reservation.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Reservation.Enums.ReservationStatus;
import com.hotel.identity_document.Model.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReservationResponse(

        Integer reservationId,

        Integer roomId,

        Id guestId,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Bogota")
        LocalDateTime checkInDate,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Bogota")
        LocalDateTime checkOutDate,

        ReservationStatus reservationStatus,

        BigDecimal roomTotalPrice

) implements Serializable {
}