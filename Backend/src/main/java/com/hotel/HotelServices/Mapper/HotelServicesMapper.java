package com.hotel.HotelServices.Mapper;

import com.hotel.HotelServices.Model.HotelServices;
import com.hotel.HotelServices.DTO.HotelServicesResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelServicesMapper {

    HotelServicesResponse toResponse(HotelServices hotelServices);
}