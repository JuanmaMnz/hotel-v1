package com.hotel.Reservation.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Reservation.DTO.ReservationRequest;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.identity_document.Model.Id;

public interface IReservationService {

    Reservation getReservationById(Integer reservationId);

    void saveReservation(Reservation reservation);

    Reservation createReservation(ReservationRequest request);

    Reservation getLatestConfirmedReservationByGuestId(Id guestId);

    Response getReservationResponseById(Integer reservationId);

    Response cancelReservation(Integer reservationId);

    Response makeReservation(ReservationRequest request);

    Response getReservationsByRoomAndMonth(Integer roomId, int year, int month, PageableRequest pageable);

}