package com.hotel.Invoice.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.GuestService.DTO.GuestServiceResponse;
import com.hotel.identity_document.Model.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InvoiceResponse(

        Integer invoiceId,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "America/Bogota")
        LocalDateTime issuedAt,

        Id guestId,

        Integer reservationId,

        LocalDateTime checkInDate,

        LocalDateTime checkOutDate,

        Integer roomId,

        BigDecimal roomPricePerNight,

        BigDecimal roomTotalPrice,

        List<GuestServiceResponse> usedServices,

        BigDecimal totalCostOfUsedServices,

        BigDecimal totalAmount

) implements Serializable {
}