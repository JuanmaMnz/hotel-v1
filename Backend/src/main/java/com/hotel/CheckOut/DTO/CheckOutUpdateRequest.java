package com.hotel.CheckOut.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOutUpdateRequest {

    @NotNull(message = "La fecha de salida es obligatoria")
    @PastOrPresent(message = "La fecha de salida no puede ser futura")
    private LocalDateTime checkOutDate;

}