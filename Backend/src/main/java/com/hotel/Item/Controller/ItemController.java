package com.hotel.Item.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Item.DTO.ItemRequest;
import com.hotel.Item.Service.IItemService;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
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
@Tag(name = "Artículos", description = "Endpoints relacionados con la gestión de artículos")
public class ItemController {

    private final IItemService itemService;

    @PostMapping("/item")
    @Operation(summary = "Registrar un nuevo artículo")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> createItem (
            @Valid @RequestBody ItemRequest request
    ) {
        Response response = itemService.createAndSaveItem(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @PutMapping("/item/{id}")
    @Operation(summary = "Actualizar un artículo existente")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updateItem (
            @PathVariable("id") Integer itemId,
            @Valid @RequestBody ItemRequest request
    ) {
        Response response = itemService.updateItem(itemId, request);
        return ResponseEntity.status(OK).body(response);
    }


    @DeleteMapping("/item/{id}")
    @Operation(summary = "Eliminar un artículo por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> deleteItem(
            @PathVariable("id") Integer itemId
    ) {
        Response response = itemService.deleteItem(itemId);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/items")
    @Operation(summary = "Listar todos los artículos")
    @WithIpRateLimit(limit = 1000, duration = 60000)
    public ResponseEntity<Response> getAllItems(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize);
        Response response = itemService.getAllItemsToResponse(pageableRequest);
        return ResponseEntity.status(OK).body(response);
    }

}