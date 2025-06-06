package com.hotel.CheckIn.Validator;

import com.hotel.CheckIn.DTO.CheckInRequest;
import com.hotel.CheckIn.Repository.CheckInRepository;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Reservation.Enums.ReservationStatus;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.hotel.Common.Exception.ErrorCode.ACTION_NOT_ALLOWED;
import static com.hotel.identity_document.Utils.IdUtils.buildId;

@Component
@RequiredArgsConstructor
public class CheckInValidator {

    private final CheckInRepository checkInRepository;

    public void validateReservationForCheckIn(Reservation reservation, CheckInRequest checkInRequest) {
        validateReservationStatus(reservation);
        validateCheckInNotExists(reservation);

        Id guestIdRequest = buildId(checkInRequest.getGuestId().getType(), checkInRequest.getGuestId().getNumber());
        validateGuest(guestIdRequest, reservation.getGuest().getGuestId());
        LocalDate actualCheckInDate = checkInRequest.getCheckInDate().toLocalDate();
        LocalDate scheduledCheckInDate = reservation.getCheckInDate().toLocalDate();
        validateCheckInDate(actualCheckInDate, scheduledCheckInDate);
    }

    private void validateCheckInDate(LocalDate actualCheckInDate, LocalDate scheduledCheckInDate) {
        if (actualCheckInDate.isBefore(scheduledCheckInDate) || actualCheckInDate.isAfter(scheduledCheckInDate)) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "La fecha de check-in no corresponde al día especificado.");
        }
    }

    private void validateCheckInNotExists(Reservation reservation) {
        if (checkInRepository.existsByReservation(reservation)) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "Ya se ha realizado un check-in para esta reserva.");
        }
    }

    private void validateReservationStatus(Reservation reservation){
        if (reservation.getReservationStatus() != ReservationStatus.CONFIRMADA) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "La reserva debe estar confirmada antes de realizar el check-in.");
        }
    }

    private void validateGuest(Id guestIdRequest, Id guestIdReservation) {
        if ( !guestIdRequest.equals(guestIdReservation) ) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "Solo el huésped que hizo la reserva puede realizar el check-in.");
        }
    }

}