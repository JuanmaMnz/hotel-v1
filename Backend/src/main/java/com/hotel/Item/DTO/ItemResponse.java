package com.hotel.Item.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ItemResponse(
        Integer id,
        String name,
        String description,
        int totalAvailableQuantity
) implements Serializable {
}