package com.hotel.GuestService.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.HotelServices.DTO.HotelServicesResponse;
import com.hotel.identity_document.Model.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GuestServiceResponse(

        HotelServicesResponse service,

        Id guestId,

        LocalDateTime usageDate

) implements Serializable {
}