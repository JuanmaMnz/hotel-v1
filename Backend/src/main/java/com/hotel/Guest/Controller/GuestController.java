package com.hotel.Guest.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.DTO.GuestUpdateRequest;
import com.hotel.Guest.DTO.RegisterGuestRequest;
import com.hotel.Guest.Service.GuestRegistrationService;
import com.hotel.Guest.Service.IGuestService;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.Enums.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.hotel.Common.Pagination.Utils.PaginationUtils.buildPageableRequest;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Huésped")
public class GuestController {

    private final IGuestService service;
    private final GuestRegistrationService guestRegistrationService;

    @Operation(summary = "Registrar un nuevo huésped")
    @PostMapping("/guest")
    @WithIpRateLimit(limit = 25, duration = 60000)
    public ResponseEntity<Response> registerNewGuest(
            @Valid @RequestBody RegisterGuestRequest request
    ) {
        Response response = guestRegistrationService.registerGuest(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @Operation(summary = "Obtener información de un huésped por tipo y número de documento")
    @GetMapping("/guest")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getGuestById(
            @RequestParam Type documentType,
            @RequestParam String documentNumber
            ) {
        IdRequest request = IdRequest.builder()
                .type(documentType)
                .number(documentNumber)
                .build();

        Response response = service.getGuestToResponseById(request);
        return ResponseEntity.status(OK).body(response);
    }


    @Operation(summary = "Obtener todos los huéspedes")
    @GetMapping("/guests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getAllGuests(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageableRequest pageableRequest = buildPageableRequest(pageNumber, pageSize);
        Response response = service.getAllGuests(pageableRequest);
        return ResponseEntity.status(OK).body(response);
    }


    @Operation(summary = "Actualizar información de un huésped")
    @PutMapping("/update-guest")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateGuest(
            @Valid @RequestBody GuestUpdateRequest request
    ) {
        Response response = service.updateGuest(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}