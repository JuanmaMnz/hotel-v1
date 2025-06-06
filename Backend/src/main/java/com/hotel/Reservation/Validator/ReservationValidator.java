package com.hotel.Reservation.Validator;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.Security.Account.Model.Account;
import com.hotel.Security.Account.Service.IAccountService;
import com.hotel.identity_document.Model.Id;

import com.hotel.CheckIn.Repository.CheckInRepository;
import com.hotel.CheckOut.Repository.CheckOutRepository;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Reservation.DTO.ReservationRequest;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.Reservation.Repository.ReservationRepository;
import com.hotel.Security.Auth.Service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.hotel.Common.Exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository reservationRepository;
    private final CheckOutRepository checkOutRepository;
    private final CheckInRepository checkInRepository;
    private final IAuthService authService;
    private final IAccountService accountService;

    public void validateFirstRequest(Id guestId, ReservationRequest request) {
        validateGuestForReservation(guestId);
        LocalDateTime checkInDate = request.getCheckInDate().atStartOfDay();
        LocalDateTime checkOutDate = request.getCheckOutDate().atStartOfDay();

        validateDate(checkInDate, checkOutDate);

        if (!isRoomAvailable(request.getRoomId(), checkInDate, checkOutDate)) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La habitación con el id '" + request.getRoomId() + "' ya está ocupada para las fechas seleccionadas.");
        }
    }

    public void validateRequest(Id guestId, ReservationRequest request, Reservation latestReservation) {
        validateGuestForReservation(guestId);
        validateGuestHasCompletedPreviousCheckOut(latestReservation);

        LocalDateTime checkInDate = request.getCheckInDate().atStartOfDay();
        LocalDateTime checkOutDate = request.getCheckOutDate().atStartOfDay();

        validateDate(checkInDate, checkOutDate);

        if (!isRoomAvailable(request.getRoomId(), checkInDate, checkOutDate)) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La habitación con el id '" + request.getRoomId() + "' ya está ocupada para las fechas seleccionadas.");
        }
    }

    public void validateCancellationRequest(Id guestId) {
        validateGuestForCancellation(guestId);
    }

    public boolean isRoomAvailable(Integer roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        return reservationRepository.countOverlappingReservations(roomId, checkIn, checkOut) == 0;
    }

    public void validateDate(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La fecha de salida debe ser posterior a la fecha de entrada.");
        }
    }

    private void validateGuestHasCompletedPreviousCheckOut(Reservation latestReservation) {
        CheckIn checkIn = checkInRepository.findByReservation(latestReservation)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No se encontró un check-in para la reserva con ID '" + latestReservation.getReservationId() + "'"));

        boolean hasCompletedCheckOut = checkOutRepository.existsByCheckIn(checkIn);
        if (!hasCompletedCheckOut) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "No puede realizar una nueva reserva hasta completar el check-out de la reserva actual.");
        }
    }

    private void validateGuestForReservation(Id guestId) {
        String authenticatedUser = authService.getCurrentUsername();
        Account account = accountService.getAccountByEmail(authenticatedUser);

        if ( !(account.getPerson().getGuest().getGuestId().equals(guestId)) ) {
            throw new InvalidRequestException(
                    ACTION_NOT_ALLOWED,
                    "El ID del huésped proporcionado para la reserva no coincide con el usuario autenticado."
            );
        }
    }

    private void validateGuestForCancellation(Id guestId) {
        String authenticatedUser = authService.getCurrentUsername();
        Account account = accountService.getAccountByEmail(authenticatedUser);

        boolean isEmployee = authService.isCurrentUserEmployee();

        if ( !(account.getPerson().getGuest().getGuestId().equals(guestId)) && !isEmployee) {
            throw new InvalidRequestException(
                    ACTION_NOT_ALLOWED,
                    "No está autorizado para cancelar esta reserva. Solo el huésped que la realizó o un empleado pueden hacerlo."
            );
        }
    }

}