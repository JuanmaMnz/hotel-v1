package com.hotel.Room.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Room.DTO.RoomRequest;
import com.hotel.Room.DTO.RoomSearchCriteria;
import com.hotel.Room.Service.IRoomService;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Habitaciones", description = "Endpoints relacionados con la gestión de habitaciones")
public class RoomController {

    private final IRoomService roomService;

    @PostMapping("/room")
    @Operation(summary = "Registrar una nueva habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> createRoom(
            @Valid @RequestBody RoomRequest request
    ) {
        Response response = roomService.createAndSaveRoom(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @PutMapping("/room/{id}")
    @Operation(summary = "Actualizar una habitación existente")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateRoom(
            @PathVariable("id") Integer roomId,
            @Valid @RequestBody RoomRequest request
    ) {
        Response response = roomService.updateRoom(roomId, request);
        return ResponseEntity.status(OK).body(response);
    }


    @DeleteMapping("/room/{id}")
    @Operation(summary = "Eliminar una habitación por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> deleteRoom(
            @PathVariable("id") Integer roomId
    ) {
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/room/{id}")
    @Operation(summary = "Obtener una habitación por ID")
    @WithIpRateLimit(limit = 1000, duration = 60000)
    public ResponseEntity<Response> getRoomById(
            @PathVariable("id") Integer roomId
    ) {
        Response response = roomService.getRoomToResponseById(roomId);
        return ResponseEntity.status(OK).body(response);
    }


    @Operation(summary = "Buscar habitaciones con filtros opcionales y paginación")
    @GetMapping("/rooms/search")
    @WithIpRateLimit(limit = 1000, duration = 60000)
    public ResponseEntity<Response> searchRooms(
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate checkOutDate,
            @RequestParam(required = false) BigDecimal minPricePerNight,
            @RequestParam(required = false) BigDecimal maxPricePerNight,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer floor,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);

        LocalDateTime checkInDateTime = checkInDate != null ? checkInDate.atStartOfDay() : null;
        LocalDateTime checkOutDateTime = checkOutDate != null ? checkOutDate.atStartOfDay().plusDays(1).minusNanos(1) : null;

        RoomSearchCriteria criteria = new RoomSearchCriteria(
                isAvailable,
                checkInDateTime,
                checkOutDateTime,
                minPricePerNight,
                maxPricePerNight,
                type,
                floor
        );

        Response response = roomService.searchRooms(criteria, pageableRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(value = "/room/{roomId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir imágenes a una habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> uploadImagesToRoom(
            @PathVariable Integer roomId,
            @RequestPart List<MultipartFile> images
    ) {
        Response response = roomService.uploadImagesToRoom(roomId, images);
        return ResponseEntity.status(CREATED).body(response);
    }


    @DeleteMapping("/room/{roomId}/images")
    @Operation(summary = "Eliminar una imagen de una habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> deleteImageFromRoom(
            @PathVariable Integer roomId,
            @RequestParam("imageUrl") String imageUrl
    ) {
        Response response = roomService.deleteImageFromRoom(roomId, imageUrl);
        return ResponseEntity.status(OK).body(response);
    }

}