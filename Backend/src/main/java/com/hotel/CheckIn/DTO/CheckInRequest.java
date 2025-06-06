package com.hotel.CheckIn.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CheckInRequest {

    @NotNull(message = "El ID del huésped es obligatorio.")
    @Valid
    private IdRequest guestId;

    @NotNull(message = "El ID del empleado es obligatorio.")
    @Valid
    private IdRequest employeeId;

    @NotNull(message = "Debe especificar si el huésped tiene acompañante.")
    private Boolean hasCompanion;

    @NotNull(message = "La fecha de ingreso es obligatoria.")
    @FutureOrPresent(message = "La fecha de ingreso no puede ser en el pasado.")
    private LocalDateTime checkInDate;

    @NotNull(message = "El ID de la reserva es obligatorio.")
    private Integer reservationId;
}