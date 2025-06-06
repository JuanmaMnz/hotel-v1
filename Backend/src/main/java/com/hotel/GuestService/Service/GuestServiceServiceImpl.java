package com.hotel.GuestService.Service;

import com.hotel.CheckIn.Model.CheckIn;
import com.hotel.CheckIn.Service.ICheckInService;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.Model.Guest;
import com.hotel.Guest.Service.IGuestService;
import com.hotel.GuestService.DTO.GuestServiceRequest;
import com.hotel.GuestService.DTO.GuestServiceResponse;
import com.hotel.GuestService.Mapper.GuestServiceMapper;
import com.hotel.GuestService.Model.GuestService;
import com.hotel.GuestService.Repository.GuestServiceRepository;
import com.hotel.GuestService.Validator.GuestServiceValidator;
import com.hotel.HotelServices.Model.HotelServices;
import com.hotel.HotelServices.Service.IHotelServicesService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.GuestService.Messages.GuestServiceResponseMessages.SERVICE_USAGE_LIST_RETRIEVED_SUCCESSFULLY;
import static com.hotel.GuestService.Messages.GuestServiceResponseMessages.SERVICE_USAGE_RECORDED_SUCCESSFULLY;
import static com.hotel.GuestService.Messages.GuestServiceResponseMessages.SERVICE_USED_BY_GUEST_DELETED_SUCCESSFULLY;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class GuestServiceServiceImpl implements IGuestServiceService{

    private final GuestServiceRepository guestServiceRepository;
    private final IGuestService guestService;
    private final IHotelServicesService hotelServicesService;
    private final ICheckInService checkInService;
    private final IReservationService reservationService;
    private final PaginationValidator paginationValidator;
    private final GuestServiceMapper guestServiceMapper;
    private final PagedResponseMapper pagedResponseMapper;
    private final GuestServiceValidator guestServiceValidator;

    @Override
    public GuestService getServiceUsedByGuest(Integer guestServiceId) {
        return guestServiceRepository.findById(guestServiceId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No se encontró el servicio utilizado por el huésped con ID '" + guestServiceId + "'"));
    }

    @Override
    public GuestService createGuestService(GuestServiceRequest request) {
        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);
        Reservation reservation =  reservationService.getReservationById(request.getReservationId());

        HotelServices service = hotelServicesService.getHotelServiceById(request.getServiceId());
        return GuestService.builder()
                .service(service)
                .guest(guest)
                .usageDate(request.getUsageDate())
                .reservation(reservation)
                .build();
    }

    @Override
    @Transactional
    public Response useService(GuestServiceRequest request) {
        Id guestId = buildId(request.getGuestId().getType(), request.getGuestId().getNumber());
        Guest guest = guestService.getGuestById(guestId);
        Reservation reservation =  reservationService.getReservationById(request.getReservationId());

        guestServiceValidator.validateUseServiceRequest(guest, reservation, request.getUsageDate());

        GuestService guestService = createGuestService(request);
        guestService.getReservation().getUsedServices().add(guestService);
        GuestService guestServiceResponse = guestServiceRepository.save(guestService);

        return generateResponse(
                "ID: " + guestServiceResponse.getGuestServiceId(),
                CREATED,
                SERVICE_USAGE_RECORDED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response getServicesUsedByGuest(Integer checkInId, PageableRequest pageableRequest) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by("usageDate").descending()
        );

        CheckIn checkIn = checkInService.getCheckInById(checkInId);

        Page<GuestServiceResponse> usedServices = guestServiceRepository
                .findServicesUsedByGuestDuringStay(
                        checkIn.getGuest().getGuestId(),
                        checkIn.getReservation().getReservationId(),
                        checkIn.getReservation().getCheckInDate(),
                        checkIn.getReservation().getCheckOutDate(),
                        pageable
                )
                .map(guestServiceMapper::guestServiceToResponse);
        PagedResponse<GuestServiceResponse> response = pagedResponseMapper.toPagedResponse(usedServices);

        return generateResponse(response, OK, SERVICE_USAGE_LIST_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public List<GuestService> getServicesUsedByGuest(Integer checkInId) {
        CheckIn checkIn = checkInService.getCheckInById(checkInId);
        return guestServiceRepository.findServicesUsedByGuestDuringStay(
                        checkIn.getGuest().getGuestId(),
                        checkIn.getReservation().getReservationId(),
                        checkIn.getReservation().getCheckInDate(),
                        checkIn.getReservation().getCheckOutDate()
                );
    }

    @Override
    public BigDecimal calculateTotalCostOfUsedServices(Integer checkInId) {
        List<GuestService> usedServices = getServicesUsedByGuest(checkInId);

        return usedServices.stream()
                .map(gs -> gs.getService().getPrice())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public Response deleteGuestService(Integer guestServiceId) {
        GuestService guestService = getServiceUsedByGuest(guestServiceId);
        guestServiceRepository.delete(guestService);
        return generateResponse(OK, SERVICE_USED_BY_GUEST_DELETED_SUCCESSFULLY);
    }

}