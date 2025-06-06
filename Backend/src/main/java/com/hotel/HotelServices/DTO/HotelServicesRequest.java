package com.hotel.HotelServices.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class HotelServicesRequest {

    @NotBlank(message = "El nombre del servicio no puede estar vacío")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "100.00", message = "El precio debe ser mayor a 100.00")
    private BigDecimal price;

    @NotBlank(message = "Los detalles del servicio no pueden estar vacíos")
    private String details;
}