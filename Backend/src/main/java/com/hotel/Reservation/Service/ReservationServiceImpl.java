package com.hotel.Reservation.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.Model.Guest;
import com.hotel.Guest.Service.IGuestService;
import com.hotel.Reservation.DTO.ReservationRequest;
import com.hotel.Reservation.DTO.ReservationResponse;
import com.hotel.Reservation.Enums.ReservationStatus;
import com.hotel.Reservation.Mapper.ReservationMapper;
import com.hotel.Reservation.Model.Reservation;
import com.hotel.Reservation.Repository.ReservationRepository;
import com.hotel.Reservation.Validator.ReservationValidator;
import com.hotel.Room.Model.Room;
import com.hotel.Room.Service.IRoomService;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Reservation.Enums.ReservationStatus.*;
import static com.hotel.Reservation.Messages.ReservationResponseMessages.*;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService{

    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationMapper reservationMapper;
    private final IRoomService roomService;
    private final IGuestService guestService;
    private final PaginationValidator paginationValidator;

    @Override
    public Reservation getReservationById(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "La reserva con ID '" + reservationId + "' no fue encontrada"));
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation createReservation(ReservationRequest request) {
        Room room = roomService.getRoomById(request.getRoomId());
        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);

        LocalDateTime checkInDate = request.getCheckInDate().atStartOfDay();
        LocalDateTime checkOutDate = request.getCheckOutDate().atStartOfDay();

        BigDecimal roomTotalPrice = roomService.calculateRoomTotalPrice(
                room.getPricePerNight(),
                checkInDate,
                checkOutDate
        );
        return Reservation.builder()
                .room(room)
                .guest(guest)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .reservationStatus(CONFIRMADA)
                .roomTotalPrice(roomTotalPrice)
                .usedServices(new ArrayList<>())
                .build();
    }

    @Override
    public Reservation getLatestConfirmedReservationByGuestId(Id guestId) {
        Guest guest = guestService.getGuestById(guestId);

        return reservationRepository
                .findTopByGuestAndReservationStatusOrderByReservationIdDesc(
                        guest,
                        ReservationStatus.CONFIRMADA)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No se encontró una reserva confirmada para el huésped con ID '" + guestId + "'"));
    }

    @Override
    public Response getReservationResponseById(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);
        ReservationResponse reservationResponse = reservationMapper.toReservationResponse(reservation);
        return generateResponse(reservationResponse, OK, RESERVATION_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response cancelReservation(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);
        Id reservationGuestId = reservation.getGuest().getGuestId();
        reservationValidator.validateCancellationRequest(reservationGuestId);
        reservation.setReservationStatus(CANCELADA);
        return generateResponse(OK, RESERVATION_CANCELLED_SUCCESSFULLY);
    }

    @Override
    public Response makeReservation(ReservationRequest request) {
        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);

        boolean isFirstReservation = reservationRepository.countNonCancelledReservationsByGuest(guest) == 0;

        if (isFirstReservation) {
            reservationValidator.validateFirstRequest(guestId, request);
        } else {
            Reservation latestReservation = getLatestConfirmedReservationByGuestId(guestId);
            reservationValidator.validateRequest(guestId, request, latestReservation);
        }

        Reservation reservation = createReservation(request);
        saveReservation(reservation);
        return generateResponse(CREATED, RESERVATION_REGISTERED_SUCCESSFULLY);
    }

    @Override
    public Response getReservationsByRoomAndMonth(
            Integer roomId,
            int year,
            int month,
            PageableRequest pageableRequest
    ) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "checkInDate")
        );

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        Page<Reservation> reservationsPage = reservationRepository.findByRoomRoomIdAndCheckInDateBetween(
                roomId, startDate, endDate, pageable
        );

        List<ReservationResponse> reservationResponses = reservationMapper.toReservationResponseList(reservationsPage.getContent());
        return generateResponse(reservationResponses, OK, RESERVATION_RETRIEVED_SUCCESSFULLY);
    }

}