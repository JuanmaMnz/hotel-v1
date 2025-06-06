package com.hotel.CheckOut.Controller;

import com.hotel.CheckOut.DTO.CheckOutRequest;
import com.hotel.CheckOut.DTO.CheckOutUpdateRequest;
import com.hotel.CheckOut.Service.ICheckOutService;
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
@Tag(name = "Check-out", description = "Endpoints para gestionar el check-out de huéspedes")
public class CheckOutController {

    private final ICheckOutService checkOutService;

    @PostMapping("/check-out")
    @Operation(summary = "Registrar un nuevo check-out")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> registerCheckOut(
            @Valid @RequestBody CheckOutRequest request
    ) {
        Response response = checkOutService.registerCheckOut(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @GetMapping("/check-out/{checkOutId}")
    @Operation(summary = "Obtener check-out por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getCheckOutById(
            @PathVariable Integer checkOutId
    ) {
        Response response = checkOutService.getCheckOutResponseById(checkOutId);
        return ResponseEntity.status(OK).body(response);
    }


    @PutMapping("/check-out/{checkOutId}")
    @Operation(summary = "Actualizar un check-out existente")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateCheckOut(
            @PathVariable Integer checkOutId,
            @Valid @RequestBody CheckOutUpdateRequest request
    ) {
        Response response = checkOutService.updateCheckOut(checkOutId, request);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/check-outs")
    @Operation(summary = "Obtener todos los check-outs de un día específico")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getAllCheckOutsByDate(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "Fecha del check-out (yyyy-MM-dd)", example = "2025-05-06")
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        Response response = checkOutService.getAllCheckOutsByDate(pageableRequest, date);
        return ResponseEntity.status(OK).body(response);
    }

}