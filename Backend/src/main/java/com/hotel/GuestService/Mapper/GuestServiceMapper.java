package com.hotel.GuestService.Mapper;

import com.hotel.GuestService.DTO.GuestServiceResponse;
import com.hotel.GuestService.Model.GuestService;
import com.hotel.HotelServices.Mapper.HotelServicesMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HotelServicesMapper.class})
public interface GuestServiceMapper {

    @Mapping(target = "guestId", source = "guest.guestId")
    @Mapping(target = "service", source = "service")
    GuestServiceResponse guestServiceToResponse(GuestService guestService);
}