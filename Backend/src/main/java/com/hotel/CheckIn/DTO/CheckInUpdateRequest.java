package com.hotel.CheckIn.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CheckInUpdateRequest {

    @NotNull(message = "Debe especificar si el huésped tiene acompañante.")
    private Boolean hasCompanion;

}