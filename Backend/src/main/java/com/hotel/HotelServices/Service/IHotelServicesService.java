package com.hotel.HotelServices.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.HotelServices.DTO.HotelServicesRequest;
import com.hotel.HotelServices.Model.HotelServices;

public interface IHotelServicesService {

    HotelServices getHotelServiceById(Integer hotelServiceId);

    HotelServices createHotelService(HotelServicesRequest request);

    Response createAndSaveHotelService(HotelServicesRequest request);

    Response getHotelServiceResponse(Integer hotelServiceId);

    Response deleteHotelService(Integer hotelServiceId);

    Response updateHotelService(Integer hotelServiceId, HotelServicesRequest request);

    Response getAllHotelServices(PageableRequest request);
}