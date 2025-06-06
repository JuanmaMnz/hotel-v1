package com.hotel.Employee.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotNull(message = "El campo 'idPersona' no puede ser nulo")
    private IdRequest personId;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @PastOrPresent(message = "La fecha de inicio no puede ser en el futuro")
    private LocalDate startDate;
}