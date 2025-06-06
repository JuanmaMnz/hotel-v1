package com.hotel.Person.DTO;

import com.hotel.Person.Annotation.AgeRange;
import com.hotel.Person.Enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    String lastName;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(
            regexp = "^\\+?\\d{7,15}$",
            message = "El número de teléfono debe tener entre 7 y 15 dígitos y debe de comenzar con '+'"
    )
    String phoneNumber;

    @NotNull(message = "El género es obligatorio")
    Gender gender;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @AgeRange(min = 1, max = 111, message = "La persona debe tener entre 1 y 110 años")
    LocalDate birthOfDate;
}