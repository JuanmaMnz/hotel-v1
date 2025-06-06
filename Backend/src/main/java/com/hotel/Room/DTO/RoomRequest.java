package com.hotel.Room.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class RoomRequest {

    @NotNull(message = "El número de habitación es obligatorio.")
    @Min(value = 1, message = "El número de habitación debe ser mayor a 0.")
    private Integer number;

    @NotBlank(message = "El tipo de habitación es obligatorio.")
    private String type;

    @NotNull(message = "El piso es obligatorio.")
    @Min(value = 0, message = "El piso no puede ser negativo.")
    private Integer floor;

    @NotNull(message = "El precio por noche es obligatorio.")
    @DecimalMin(value = "100.00", message = "El precio debe ser mayor a 100.00")
    private BigDecimal pricePerNight;
}