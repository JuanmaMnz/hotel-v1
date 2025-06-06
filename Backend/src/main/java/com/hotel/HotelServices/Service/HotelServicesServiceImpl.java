package com.hotel.HotelServices.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.HotelServices.DTO.HotelServicesRequest;
import com.hotel.HotelServices.Mapper.HotelServicesMapper;
import com.hotel.HotelServices.Model.HotelServices;
import com.hotel.HotelServices.DTO.HotelServicesResponse;
import com.hotel.HotelServices.Repository.HotelServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.HotelServices.Messages.HotelServicesResponseMessages.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class HotelServicesServiceImpl implements IHotelServicesService {

    private final HotelServicesRepository hotelServicesRepository;
    private final HotelServicesMapper hotelServicesMapper;
    private final PagedResponseMapper pagedResponseMapper;
    private final PaginationValidator paginationValidator;

    @Override
    public HotelServices getHotelServiceById(Integer hotelServiceId) {
        return hotelServicesRepository.findById(hotelServiceId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El servicio con ID " + hotelServiceId + " no existe"));
    }

    @Override
    public HotelServices createHotelService(HotelServicesRequest request) {
        return HotelServices.builder()
                .name(request.getName())
                .price(request.getPrice())
                .details(request.getDetails())
                .build();
    }

    @Override
    public Response createAndSaveHotelService(HotelServicesRequest request) {
        HotelServices hotelServices = createHotelService(request);
        hotelServicesRepository.save(hotelServices);
        return generateResponse(CREATED, HOTEL_SERVICE_REGISTERED_SUCCESSFULLY);
    }

    @Override
    public Response getHotelServiceResponse(Integer hotelServiceId) {
        HotelServices hotelServices = getHotelServiceById(hotelServiceId);
        HotelServicesResponse response = hotelServicesMapper.toResponse(hotelServices);
        return generateResponse(response, OK, HOTEL_SERVICE_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response deleteHotelService(Integer hotelServiceId) {
        HotelServices hotelServices = getHotelServiceById(hotelServiceId);
        hotelServicesRepository.delete(hotelServices);
        return generateResponse(OK, HOTEL_SERVICE_DELETED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateHotelService(Integer hotelServiceId, HotelServicesRequest request) {
        HotelServices existingHotelServices = getHotelServiceById(hotelServiceId);
        existingHotelServices.setName(request.getName());
        existingHotelServices.setPrice(request.getPrice());
        existingHotelServices.setDetails(request.getDetails());
        return generateResponse(OK, HOTEL_SERVICE_UPDATED_SUCCESSFULLY);
    }

    @Override
    public Response getAllHotelServices(PageableRequest request) {
        paginationValidator.validatePagination(request.getPageNumber(), request.getPageSize());
        Pageable pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                Sort.by(Sort.Direction.ASC, "name")
        );

        Page<HotelServices> hotelServicesPage = hotelServicesRepository.findAll(pageable);
        Page<HotelServicesResponse> hotelServiceResponses = hotelServicesPage.map(hotelServicesMapper::toResponse);
        PagedResponse<HotelServicesResponse> response = pagedResponseMapper.toPagedResponse(hotelServiceResponses);
        return generateResponse(response, OK, HOTEL_SERVICE_RETRIEVED_SUCCESSFULLY);
    }

}