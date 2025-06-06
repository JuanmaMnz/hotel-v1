package com.hotel.Common.Pagination.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableRequest {

    @NotNull(message = "El número de página es obligatorio.")
    @Min(value = 0, message = "El número de página no puede ser negativo.")
    int pageNumber;

    @NotNull(message = "El tamaño de página es obligatorio.")
    @Min(value = 1, message = "El tamaño mínimo de página debe ser 1.")
    @Max(value = 50, message = "El tamaño máximo de página debe ser 100.")
    int pageSize;
}