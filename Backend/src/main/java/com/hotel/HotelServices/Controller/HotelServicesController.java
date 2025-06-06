package com.hotel.HotelServices.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.HotelServices.DTO.HotelServicesRequest;
import com.hotel.HotelServices.Service.IHotelServicesService;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.hotel.Common.Pagination.Utils.PaginationUtils.buildPageableRequest;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Servicios de Hotel", description = "Endpoints para gestionar los servicios del hotel")
public class HotelServicesController {

    private final IHotelServicesService service;

    @PostMapping("/service")
    @Operation(summary = "Crear un nuevo servicio de hotel")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> createAndSaveHotelService(
            @Valid @RequestBody HotelServicesRequest request
    ) {
        Response response = service.createAndSaveHotelService(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @GetMapping("/service/{hotelServiceId}")
    @Operation(summary = "Obtener servicio de hotel por ID")
    @WithIpRateLimit(limit = 1000, duration = 60000)
    public ResponseEntity<Response> getHotelService(
            @PathVariable Integer hotelServiceId
    ) {
        Response response = service.getHotelServiceResponse(hotelServiceId);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/services")
    @Operation(summary = "Obtener todos los servicios de hotel")
    @WithIpRateLimit(limit = 1000, duration = 60000)
    public ResponseEntity<Response>  getAllHotelServices(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageableRequest pageableRequest = buildPageableRequest(pageNumber, pageSize);
        Response response = service.getAllHotelServices(pageableRequest);
        return ResponseEntity.status(OK).body(response);
    }


    @PutMapping("/service/{hotelServiceId}")
    @Operation(summary = "Actualizar un servicio de hotel existente")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateHotelService(
            @PathVariable Integer hotelServiceId,
            @Valid @RequestBody HotelServicesRequest request
    ) {
        Response response = service.updateHotelService(hotelServiceId, request);
        return ResponseEntity.status(OK).body(response);
    }


    @DeleteMapping("/service/{hotelServiceId}")
    @Operation(summary = "Eliminar un servicio de hotel")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> deleteHotelService(
            @PathVariable Integer hotelServiceId
    ) {
        Response response = service.deleteHotelService(hotelServiceId);
        return ResponseEntity.status(OK).body(response);
    }

}