package com.hotel.HotelServices.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HotelServicesResponse(

        Integer serviceId,

        String name,

        BigDecimal price,

        String details

) implements Serializable {
}