package com.hotel.CheckIn.Service;

import com.hotel.CheckIn.DTO.CheckInRequest;
import com.hotel.CheckIn.DTO.CheckInResponse;
import com.hotel.CheckIn.DTO.CheckInUpdateRequest;
import com.hotel.CheckIn.Mapper.CheckInMapper;
import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckIn.Repository.CheckInRepository;
import com.hotel.CheckIn.Validator.CheckInValidator;
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
import com.hotel.Reservation.Model.Reservation;
import com.hotel.Reservation.Service.IReservationService;
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

import static com.hotel.CheckIn.Messages.CheckInResponseMessages.*;
import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements  ICheckInService{

    private final CheckInRepository checkInRepository;
    private final CheckInMapper checkInMapper;
    private final IGuestService guestService;
    private final IReservationService reservationService;
    private final IEmployeeService employeeService;
    private final PagedResponseMapper pagedResponseMapper;
    private final PaginationValidator paginationValidator;
    private final CheckInValidator checkInValidator;

    @Override
    public CheckIn getCheckInById(Integer checkInId) {
        return checkInRepository.findById(checkInId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El check-in con ID '" + checkInId + "' no fue encontrado."));
    }

    @Override
    public CheckIn getCheckInByReservation(Reservation reservation) {
        return checkInRepository.findByReservation(reservation)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No se encontró un check-in para la reserva con ID '" + reservation.getReservationId() + "'"));
    }

    @Override
    public CheckIn createCheckIn(CheckInRequest request) {
        Reservation reservation = reservationService.getReservationById(request.getReservationId());

        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);

        Id employeeId = buildId(request.getEmployeeId().getType(), request.getEmployeeId().getNumber());
        Employee employee = employeeService.getEmployeeById(employeeId);

        return CheckIn.builder()
                .guest(guest)
                .reservation(reservation)
                .employee(employee)
                .hasCompanion(request.getHasCompanion())
                .checkInDate(request.getCheckInDate())
                .build();
    }

    @Override
    public Response getCheckInResponseById(Integer checkInId) {
        CheckIn checkIn = getCheckInById(checkInId);
        CheckInResponse checkInResponse = checkInMapper.checkInToResponse(checkIn);
        return generateResponse(checkInResponse, OK, CHECKIN_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public Response registerCheckIn(CheckInRequest request) {
        Reservation reservation = reservationService.getReservationById(request.getReservationId());
        CheckIn checkIn = createCheckIn(request);
        checkInValidator.validateReservationForCheckIn(reservation, request);
        CheckIn checkInResponse = checkInRepository.save(checkIn);
        return generateResponse(
                "Check-in Id: " + checkInResponse.getCheckInId(),
                CREATED,
                CHECKIN_REGISTERED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateCheckIn(Integer checkInId, CheckInUpdateRequest request) {
        CheckIn checkIn = getCheckInById(checkInId);
        checkIn.setHasCompanion(request.getHasCompanion());
        return generateResponse(OK, CHECKIN_UPDATED_SUCCESSFULLY);
    }

    @Override
    public Response getAllCheckInsByDate(PageableRequest pageableRequest, LocalDate day) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "checkInDate")
        );

        LocalDateTime startDateTime = day.atStartOfDay();
        LocalDateTime endDateTime = day.atTime(LocalTime.MAX);

        Page<CheckIn> checkInsPage = checkInRepository.findAllByCheckInDateBetween(startDateTime, endDateTime, pageable);
        Page<CheckInResponse> checkInResponses = checkInsPage.map(checkInMapper::checkInToResponse);
        PagedResponse<CheckInResponse> response = pagedResponseMapper.toPagedResponse(checkInResponses);

        return generateResponse(response, OK, CHECKIN_LIST_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public boolean hasCompletedCheckIn(Id guestId) {
        return checkInRepository.hasCompletedCheckIn(guestId);
    }

}