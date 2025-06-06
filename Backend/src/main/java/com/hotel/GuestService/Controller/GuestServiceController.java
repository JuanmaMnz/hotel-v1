package com.hotel.GuestService.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.GuestService.DTO.GuestServiceRequest;
import com.hotel.GuestService.Service.IGuestServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Uso de servicios", description = "Endpoints para gestionar los servicios utilizados por los huéspedes")
public class GuestServiceController {

    private final IGuestServiceService guestServiceService;

    @PostMapping("/guest/service")
    @Operation(summary = "Registrar el uso de un servicio por parte de un huésped")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> useService(
            @Valid @RequestBody GuestServiceRequest request
    ) {
        Response response = guestServiceService.useService(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @GetMapping("/guest/services")
    @Operation(summary = "Obtener los servicios utilizados por un huésped durante su estadía")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getServicesUsedByGuest(
            @RequestParam Integer checkInId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        Response response = guestServiceService.getServicesUsedByGuest(checkInId, pageableRequest);
        return ResponseEntity.status(OK).body(response);
    }


    @DeleteMapping("/guest/services/{guestServiceId}")
    @Operation(summary = "Eliminar un servicio utilizado por un huésped")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> deleteGuestService(
            @PathVariable Integer guestServiceId
    ) {
        Response response = guestServiceService.deleteGuestService(guestServiceId);
        return ResponseEntity.status(OK).body(response);
    }

}