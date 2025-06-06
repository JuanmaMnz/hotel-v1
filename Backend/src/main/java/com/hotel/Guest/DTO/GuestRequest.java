package com.hotel.Guest.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestRequest {

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

}