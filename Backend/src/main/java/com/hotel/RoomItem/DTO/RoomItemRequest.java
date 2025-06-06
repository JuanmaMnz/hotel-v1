package com.hotel.RoomItem.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomItemRequest {

    @Positive(message = "La cantidad debe ser un número positivo")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    int quantity;

}