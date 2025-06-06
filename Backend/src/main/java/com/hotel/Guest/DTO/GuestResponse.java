package com.hotel.Guest.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Person.DTO.PersonResponse;
import com.hotel.identity_document.Model.Id;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GuestResponse(

        Id guestId,

        PersonResponse personalInformation,

        String address

) implements Serializable {
}