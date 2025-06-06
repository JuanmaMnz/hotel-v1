package com.hotel.CheckIn.Controller;

import com.hotel.CheckIn.DTO.CheckInRequest;
import com.hotel.CheckIn.DTO.CheckInUpdateRequest;
import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Check-in", description = "Endpoints para gestionar el check-in de huéspedes")
public class CheckInController {

    private final ICheckInService checkInService;

    @GetMapping("/check-in/{checkInId}")
    @Operation(summary = "Obtener check-in por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getCheckInById(
            @PathVariable Integer checkInId
    ) {
        Response response = checkInService.getCheckInResponseById(checkInId);
        return ResponseEntity.status(OK).body(response);
    }


    @PostMapping("/check-in")
    @Operation(summary = "Registrar un nuevo check-in")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> registerCheckIn(
            @Valid @RequestBody CheckInRequest request
    ) {
        Response response = checkInService.registerCheckIn(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @PutMapping("/check-in/{checkInId}")
    @Operation(summary = "Actualizar un check-in existente")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateCheckIn(
            @PathVariable Integer checkInId,
            @Valid @RequestBody CheckInUpdateRequest request
    ) {
        Response response = checkInService.updateCheckIn(checkInId, request);
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/check-ins")
    @Operation(summary = "Obtener todos los check-ins de un día específico")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getAllCheckInsByDate(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "Fecha del check-in (yyyy-MM-dd)", example = "2025-05-06")
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        Response response = checkInService.getAllCheckInsByDate(pageableRequest, date);
        return ResponseEntity.status(OK).body(response);
    }

}