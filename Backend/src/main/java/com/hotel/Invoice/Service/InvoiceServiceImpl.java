package com.hotel.Invoice.Service;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.Model.Guest;
import com.hotel.Guest.Service.IGuestService;
import com.hotel.GuestService.Model.GuestService;
import com.hotel.GuestService.Service.IGuestServiceService;
import com.hotel.Invoice.DTO.InvoiceRequest;
import com.hotel.Invoice.DTO.InvoiceResponse;
import com.hotel.Invoice.Mapper.InvoiceMapper;
import com.hotel.Invoice.Model.Invoice;
import com.hotel.Invoice.Repository.InvoiceRepository;
import com.hotel.Invoice.Validator.InvoiceValidator;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Invoice.Messages.InvoiceResponseMessages.INVOICE_RETRIEVED_SUCCESSFULLY;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final IGuestService guestService;
    private final ICheckInService checkInService;
    private final IGuestServiceService guestServiceService;
    private final InvoiceValidator invoiceValidator;

    @Override
    public Invoice createInvoice(InvoiceRequest request) {
        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);

        CheckIn checkIn = checkInService.getCheckInById(request.getCheckInId());
        invoiceValidator.validateInvoiceRequest(request, checkIn);

        List<GuestService> usedServices = guestServiceService.getServicesUsedByGuest(checkIn.getCheckInId());
        BigDecimal totalCostOfUsedServices = guestServiceService.calculateTotalCostOfUsedServices(checkIn.getCheckInId());
        BigDecimal roomTotalPrice = checkIn.getReservation().getRoomTotalPrice();
        BigDecimal totalPrice = roomTotalPrice.add(totalCostOfUsedServices);

        return buildInvoice(guest, checkIn, usedServices, totalCostOfUsedServices, totalPrice);
    }

    private Invoice buildInvoice(
            Guest guest,
            CheckIn checkIn,
            List<GuestService> usedServices,
            BigDecimal totalUsedServicesCost,
            BigDecimal totalAmount
    ) {
        Invoice invoice = Invoice.builder()
                .issuedAt(LocalDateTime.now())
                .reservation(checkIn.getReservation())
                .guest(guest)
                .totalCostOfUsedServices(totalUsedServicesCost)
                .totalAmount(totalAmount)
                .build();

        usedServices.forEach(service -> service.setInvoice(invoice));
        invoice.setUsedServices(usedServices);

        return invoice;
    }

    @Override
    @Transactional
    public Invoice createAndSaveInvoice(InvoiceRequest request) {
        Invoice invoice = createInvoice(request);
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice getInvoice(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND ,
                        "La Factura con ID '" + invoiceId + "' no fue encontrada "));
    }

    @Override
    @Transactional
    public Response getInvoiceById(Integer invoiceId) {
        Invoice invoice = getInvoice(invoiceId);
        InvoiceResponse invoiceResponse = invoiceMapper.InvoiceToResponse(invoice);
        return generateResponse(invoiceResponse, OK, INVOICE_RETRIEVED_SUCCESSFULLY);
    }

}