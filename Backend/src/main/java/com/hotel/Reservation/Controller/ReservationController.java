package com.hotel.Reservation.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Reservation.DTO.ReservationRequest;
import com.hotel.Reservation.Service.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Endpoints relacionados con la gestión de reservas")
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping("/reservation")
    @Operation(summary = "Hacer una reserva")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'GUEST')")
    public ResponseEntity<Response> createReservation(
            @Valid @RequestBody ReservationRequest request
    ) {
        Response response = reservationService.makeReservation(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @GetMapping("/reservation/{id}")
    @Operation(summary = "Obtener una reserva por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getReservationById(
            @PathVariable("id") Integer reservationId
    ) {
        Response response = reservationService.getReservationResponseById(reservationId);
        return ResponseEntity.status(OK).body(response);
    }


    @PatchMapping("/reservation/{reservationId}/update-state-to-cancel")
    @Operation(summary = "Actualizar el estado de una reserva a CANCELADA")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'GUEST')")
    public ResponseEntity<Response> cancelReservation(
            @PathVariable("reservationId") Integer reservationId
    ) {
        Response response = reservationService.cancelReservation(reservationId);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/reservation/by-room-month")
    @Operation(summary = "Obtener las reservas de una habitación para un mes específico")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getReservationsByRoomAndMonth(
            @RequestParam Integer roomId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        Response response = reservationService.getReservationsByRoomAndMonth(roomId, year, month, pageableRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}