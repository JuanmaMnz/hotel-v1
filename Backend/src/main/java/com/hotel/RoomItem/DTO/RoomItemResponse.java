package com.hotel.RoomItem.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoomItemResponse(
        Integer id,
        String itemName,
        String itemDescription,
        int itemQuantity
) implements Serializable {
}
