package com.hotel.CheckOut.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOutRequest {

    @NotNull(message = "El ID del huésped es obligatorio")
    private IdRequest guestId;

    @NotNull(message = "El ID del empleado es obligatorio")
    private IdRequest employeeId;

    @NotNull(message = "El ID del check-in es obligatorio")
    private Integer checkInId;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime checkOutDate;

}