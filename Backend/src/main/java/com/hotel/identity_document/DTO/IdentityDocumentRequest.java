package com.hotel.identity_document.DTO;

import com.hotel.identity_document.Enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class IdentityDocumentRequest {

    @NotNull(message = "El tipo de documento es obligatorio.")
    private Type type;

    @NotBlank(message = "El número del documento no puede estar vacío.")
    private String number;

    @NotBlank(message = "El país emisor del documento es obligatorio.")
    private String issuingCountry;

    @NotNull(message = "La fecha de emisión del documento es obligatoria.")
    private LocalDate issueDate;
}