package com.hotel.Invoice.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceRequest {

    @NotNull(message = "El ID del huésped no puede ser nulo")
    @Valid
    IdRequest guestId;

    @NotNull(message = "El ID de check-in no puede ser nulo")
    @Min(value = 1, message = "El ID de check-in debe ser mayor que 0")
    Integer checkInId;
}