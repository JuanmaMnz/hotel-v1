package com.hotel.CheckIn.Service;

import com.hotel.CheckIn.DTO.CheckInRequest;
import com.hotel.CheckIn.DTO.CheckInUpdateRequest;
import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.identity_document.Model.Id;


import java.time.LocalDate;

public interface ICheckInService {

    CheckIn getCheckInById(Integer checkInId);

    CheckIn getCheckInByReservation(Reservation reservation);

    CheckIn createCheckIn(CheckInRequest request);

    Response getCheckInResponseById(Integer checkInId);

    Response registerCheckIn(CheckInRequest request);

    Response updateCheckIn(Integer checkInId, CheckInUpdateRequest request);

    Response getAllCheckInsByDate(PageableRequest pageableRequest, LocalDate date);

    boolean hasCompletedCheckIn(Id guestId);
}