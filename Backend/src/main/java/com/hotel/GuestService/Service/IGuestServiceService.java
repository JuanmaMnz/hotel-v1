package com.hotel.GuestService.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.GuestService.DTO.GuestServiceRequest;
import com.hotel.GuestService.Model.GuestService;

import java.math.BigDecimal;
import java.util.List;

public interface IGuestServiceService {

    GuestService getServiceUsedByGuest(Integer guestServiceId);

    GuestService createGuestService(GuestServiceRequest request);

    List<GuestService> getServicesUsedByGuest(Integer checkInId);

    BigDecimal calculateTotalCostOfUsedServices(Integer checkInId);

    Response useService(GuestServiceRequest request);

    Response getServicesUsedByGuest(Integer checkInId, PageableRequest pageableRequest);

    Response deleteGuestService(Integer guestServiceId);
}