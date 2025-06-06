package com.hotel.CheckOut.Mapper;

import com.hotel.CheckOut.DTO.CheckOutResponse;
import com.hotel.CheckOut.Model.CheckOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CheckOutMapper {

    @Mapping(source = "guest.guestId", target = "guestId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    @Mapping(source = "invoice.invoiceId", target = "invoiceId")
    CheckOutResponse checkOutToResponse(CheckOut checkOut);
}