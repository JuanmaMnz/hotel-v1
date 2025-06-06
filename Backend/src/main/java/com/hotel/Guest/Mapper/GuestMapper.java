package com.hotel.Guest.Mapper;

import com.hotel.Guest.DTO.GuestResponse;
import com.hotel.Guest.Model.Guest;
import com.hotel.Person.Mapper.PersonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface GuestMapper {

    @Mapping(source = "person", target = "personalInformation")
    GuestResponse guestToResponse(Guest guest);
}