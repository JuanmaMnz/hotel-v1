package com.hotel.RoomItem.Controller;

import com.hotel.Common.Response.Response;
import com.hotel.RoomItem.DTO.RoomItemRequest;
import com.hotel.RoomItem.Service.IRoomItemService;
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
@Tag(name = "Artículos en habitaciones", description = "Endpoints para gestionar artículos en habitaciones")
public class RoomItemController {

    private final IRoomItemService roomItemService;

    @PostMapping("/room/{roomId}/item/{itemId}")
    @Operation(summary = "Asignar un artículo a una habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> assignItemToRoom(
            @PathVariable Integer roomId,
            @PathVariable Integer itemId,
            @Valid @RequestBody RoomItemRequest request
    ) {
        Response response = roomItemService.assignItemToRoom(roomId, itemId, request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @DeleteMapping("/room/{roomId}/item/{itemId}")
    @Operation(summary = "Eliminar un artículo de una habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> removeItemFromRoom(
            @PathVariable Integer roomId,
            @PathVariable Integer itemId
    ) {
        Response response = roomItemService.removeItemFromRoom(roomId, itemId);
        return ResponseEntity.status(OK).body(response);
    }


    @PutMapping("/room/{roomId}/item/{itemId}")
    @Operation(summary = "Actualizar un artículo en una habitación")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateItemInRoom(
            @PathVariable Integer roomId,
            @PathVariable Integer itemId,
            @Valid @RequestBody RoomItemRequest request
    ) {
        Response response = roomItemService.updateItemInRoom(roomId, itemId, request);
        return ResponseEntity.status(OK).body(response);
    }

}