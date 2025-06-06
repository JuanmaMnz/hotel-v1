package com.hotel.Reservation.Mapper;

import com.hotel.Reservation.DTO.ReservationResponse;
import com.hotel.Reservation.Model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "room.roomId", target = "roomId")
    @Mapping(source = "guest.guestId", target= "guestId")
    ReservationResponse toReservationResponse(Reservation reservation);

    List<ReservationResponse> toReservationResponseList(List<Reservation> reservations);
}