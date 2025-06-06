package com.hotel.Invoice.Validator;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Invoice.DTO.InvoiceRequest;
import com.hotel.Invoice.Repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.ACTION_NOT_ALLOWED;

@Component
@RequiredArgsConstructor
public class InvoiceValidator {

    private final InvoiceRepository invoiceRepository;

    public void validateInvoiceRequest(InvoiceRequest invoiceRequest, CheckIn checkIn) {

        if (invoiceRepository.existsByReservation(checkIn.getReservation()) ) {
            throw new InvalidRequestException(ACTION_NOT_ALLOWED,
                    "Ya existe una factura asociada a esta reserva. No se puede generar otra."
            );
        }
    }

}