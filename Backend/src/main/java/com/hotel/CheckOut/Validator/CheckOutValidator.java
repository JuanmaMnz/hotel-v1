package com.hotel.CheckOut.Validator;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.CheckOut.DTO.CheckOutRequest;
import com.hotel.CheckOut.Repository.CheckOutRepository;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.Reservation.Service.IReservationService;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.ACTION_NOT_ALLOWED;

@Component
@RequiredArgsConstructor
public class CheckOutValidator {

    private final CheckOutRepository checkOutRepository;
    private final IReservationService reservationService;
    private final ICheckInService checkInService;

    public void validateCheckOutRequest(CheckOutRequest request, CheckIn checkIn) {
        Integer reservationId = checkIn.getReservation().getReservationId();
        if (checkOutRepository.existsByReservationId(reservationId)) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "Ya existe un check-out registrado para esta reserva.");
        }
    }

    public boolean hasCompletedCheckOut(Id guestId) {
        Reservation latestReservation = reservationService.getLatestConfirmedReservationByGuestId(guestId);
        CheckIn checkIn = checkInService.getCheckInByReservation(latestReservation);
        return checkOutRepository.existsByCheckIn(checkIn);
    }

}