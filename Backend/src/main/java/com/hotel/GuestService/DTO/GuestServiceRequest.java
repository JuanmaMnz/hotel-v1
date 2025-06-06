package com.hotel.GuestService.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class GuestServiceRequest {

    @NotNull(message = "El ID del servicio es obligatorio")
    @Positive(message = "El ID del servicio debe ser un número positivo")
    Integer serviceId;

    @NotNull(message = "El ID del servicio es obligatorio")
    @Positive(message = "El ID del servicio debe ser un número positivo")
    Integer reservationId;

    @Valid
    @NotNull(message = "La información del huésped es obligatoria")
    IdRequest guestId;

    @NotNull(message = "La fecha de uso del servicio es obligatoria")
    LocalDateTime usageDate;
}