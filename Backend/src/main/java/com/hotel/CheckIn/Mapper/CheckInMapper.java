package com.hotel.CheckIn.Mapper;

import com.hotel.CheckIn.DTO.CheckInResponse;
import com.hotel.CheckIn.Model.CheckIn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CheckInMapper {

    @Mapping(source = "guest.guestId", target = "guestId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(source = "reservation.reservationId", target = "reservationId")
    CheckInResponse checkInToResponse(CheckIn checkIn);
}