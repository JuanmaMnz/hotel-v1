package com.hotel.Room.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.RoomItem.DTO.RoomItemResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoomResponse(

        Integer roomId,

        Integer number,

        String type,

        Integer floor,

        BigDecimal pricePerNight,

        Set<RoomItemResponse> items,

        Set<String> imagesUrls

) implements Serializable {
}