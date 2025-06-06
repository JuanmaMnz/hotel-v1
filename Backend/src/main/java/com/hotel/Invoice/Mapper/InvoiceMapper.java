package com.hotel.Invoice.Mapper;

import com.hotel.GuestService.Mapper.GuestServiceMapper;
import com.hotel.Invoice.DTO.InvoiceResponse;
import com.hotel.Invoice.Model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GuestServiceMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "guestId", source = "guest.guestId")
    @Mapping(target = "reservationId", source = "reservation.reservationId")
    @Mapping(target = "checkInDate", source = "reservation.checkInDate")
    @Mapping(target = "checkOutDate", source = "reservation.checkOutDate")
    @Mapping(target = "roomId", source = "reservation.room.roomId")
    @Mapping(target = "roomPricePerNight", source = "reservation.room.pricePerNight")
    @Mapping(target = "roomTotalPrice", source = "reservation.roomTotalPrice")
    @Mapping(target = "usedServices", source = "usedServices")
    InvoiceResponse InvoiceToResponse(Invoice invoice);
}