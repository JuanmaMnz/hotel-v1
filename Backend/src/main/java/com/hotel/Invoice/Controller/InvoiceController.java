package com.hotel.Invoice.Controller;

import com.hotel.Common.Response.Response;
import com.hotel.Invoice.Service.IInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Facturación", description = "Operaciones relacionadas con facturación")
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @Operation(summary = "Obtener una factura por su ID")
    @GetMapping("invoice/{invoiceId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getInvoiceById(
            @PathVariable Integer invoiceId
    ) {
        Response response = invoiceService.getInvoiceById(invoiceId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}