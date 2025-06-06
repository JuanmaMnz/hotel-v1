package com.hotel.identity_document.DTO;

import com.hotel.identity_document.Enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdRequest {

    @NotNull(message = "El tipo de documento es obligatorio.")
    private Type type;

    @NotBlank(message = "El número del documento no puede estar vacío.")
    private String number;
}
