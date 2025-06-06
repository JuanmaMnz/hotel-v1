package com.hotel.Reservation.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {

    @NotNull(message = "El ID de la habitación es obligatorio")
    Integer roomId;

    @NotNull
    IdRequest guestId;

    @NotNull(message = "La fecha de entrada es obligatoria")
    @FutureOrPresent(message = "La fecha de entrada debe ser en el presente o en el futuro")
    LocalDate checkInDate;

    @NotNull(message = "La fecha de salida es obligatoria")
    @Future(message = "La fecha de salida debe ser en el futuro")
    LocalDate checkOutDate;
}