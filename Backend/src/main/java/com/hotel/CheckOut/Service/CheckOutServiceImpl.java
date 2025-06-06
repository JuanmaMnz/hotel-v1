package com.hotel.CheckOut.Service;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.CheckOut.DTO.CheckOutRequest;
import com.hotel.CheckOut.DTO.CheckOutResponse;
import com.hotel.CheckOut.DTO.CheckOutUpdateRequest;
import com.hotel.CheckOut.Mapper.CheckOutMapper;
import com.hotel.CheckOut.Model.CheckOut;
import com.hotel.CheckOut.Repository.CheckOutRepository;
import com.hotel.CheckOut.Validator.CheckOutValidator;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Employee.Model.Employee;
import com.hotel.Employee.Service.IEmployeeService;
import com.hotel.Guest.Model.Guest;
import com.hotel.Guest.Service.IGuestService;
import com.hotel.Invoice.DTO.InvoiceRequest;
import com.hotel.Invoice.DTO.InvoiceResponse;
import com.hotel.Invoice.Mapper.InvoiceMapper;
import com.hotel.Invoice.Model.Invoice;
import com.hotel.Invoice.Service.IInvoiceService;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.hotel.CheckOut.Messages.CheckOutResponseMessages.*;
import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements ICheckOutService {

    private final CheckOutRepository checkOutRepository;
    private final CheckOutMapper checkOutMapper;
    private final CheckOutValidator checkOutValidator;
    private final ICheckInService checkInService;
    private final IGuestService guestService;
    private final IEmployeeService employeeService;
    private final IInvoiceService invoiceService;
    private final PaginationValidator paginationValidator;
    private final PagedResponseMapper pagedResponseMapper;
    private final InvoiceMapper invoiceMapper;

    @Override
    public CheckOut getCheckOutById(Integer checkOutId) {
        return checkOutRepository.findById(checkOutId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El check-out con ID '" + checkOutId + "' no fue encontrado."));
    }

    @Override
    public CheckOut createCheckOut(CheckOutRequest request) {
        CheckIn checkIn = checkInService.getCheckInById(request.getCheckInId());

        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);

        Id employeeId = buildId(request.getEmployeeId().getType(), request.getEmployeeId().getNumber());
        Employee employee = employeeService.getEmployeeById(employeeId);

        InvoiceRequest invoiceRequest = InvoiceRequest.builder()
                .guestId(request.getGuestId())
                .checkInId(checkIn.getCheckInId())
                .build();
        Invoice invoice = invoiceService.createAndSaveInvoice(invoiceRequest);

        return CheckOut.builder()
                .checkIn(checkIn)
                .guest(guest)
                .employee(employee)
                .invoice(invoice)
                .checkOutDate(request.getCheckOutDate())
                .build();
    }

    @Override
    public Response getCheckOutResponseById(Integer checkOutId) {
        CheckOut checkOut = getCheckOutById(checkOutId);
        CheckOutResponse checkOutResponse = checkOutMapper.checkOutToResponse(checkOut);
        return generateResponse(checkOutResponse, OK, CHECKOUT_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response registerCheckOut(CheckOutRequest request) {
        CheckIn checkIn = checkInService.getCheckInById(request.getCheckInId());
        checkOutValidator.validateCheckOutRequest(request, checkIn);
        CheckOut checkOut = createCheckOut(request);
        CheckOut savedCheckOut = checkOutRepository.save(checkOut);

        InvoiceResponse invoiceResponse = invoiceMapper.InvoiceToResponse(savedCheckOut.getInvoice());
        return generateResponse(
                invoiceResponse,
                CREATED,
                CHECKOUT_REGISTERED_SUCCESSFULLY
        );
    }

    @Override
    @Transactional
    public Response updateCheckOut(Integer checkOutId, CheckOutUpdateRequest request) {
        CheckOut checkOut = getCheckOutById(checkOutId);
        checkOut.setCheckOutDate(request.getCheckOutDate());
        return generateResponse(OK, CHECKOUT_UPDATED_SUCCESSFULLY);
    }

    @Override
    public Response getAllCheckOutsByDate(PageableRequest pageableRequest, LocalDate date) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());

        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "checkOutDate")
        );

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        Page<CheckOut> checkOutPage = checkOutRepository.findByCheckOutDateRange(startDateTime, endDateTime, pageable);
        Page<CheckOutResponse> checkOutResponses = checkOutPage.map(checkOutMapper::checkOutToResponse);
        PagedResponse<CheckOutResponse> response = pagedResponseMapper.toPagedResponse(checkOutResponses);

        return generateResponse(response, OK, CHECKOUT_LIST_RETRIEVED_SUCCESSFULLY);
    }

}