package com.hotel.GuestService.Validator;

import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.CheckOut.Validator.CheckOutValidator;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Guest.Model.Guest;
import com.hotel.Reservation.Model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.hotel.Common.Exception.ErrorCode.ACTION_NOT_ALLOWED;

@Component
@RequiredArgsConstructor
public class GuestServiceValidator {

    private final ICheckInService checkInService;
    private final CheckOutValidator checkOutValidator;

    public void validateUseServiceRequest(Guest guest, Reservation reservation, LocalDateTime usageDate) {
        if ( !checkInService.hasCompletedCheckIn(guest.getGuestId()) ){
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "El huésped debe realizar el check-in antes de acceder a este servicio.");
        }

        if ( checkOutValidator.hasCompletedCheckOut(guest.getGuestId()) ) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "El huésped ya realizó el check-out. No puede acceder a servicios adicionales.");
        }

        if (usageDate.isBefore(reservation.getCheckInDate()) || usageDate.isAfter(reservation.getCheckOutDate())) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "La fecha de uso del servicio debe estar dentro del período de la reserva.");
        }
    }

}