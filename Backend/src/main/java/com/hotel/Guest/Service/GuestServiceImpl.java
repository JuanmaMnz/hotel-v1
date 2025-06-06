package com.hotel.Guest.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.DTO.GuestResponse;
import com.hotel.Guest.DTO.GuestUpdateRequest;
import com.hotel.Guest.Mapper.GuestMapper;
import com.hotel.Guest.Model.Guest;
import com.hotel.Guest.Repository.GuestRepository;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Guest.Messages.GuestResponseMessages.*;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements IGuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;
    private final PaginationValidator paginationValidator;
    private final PagedResponseMapper pagedResponseMapper;

    @Override
    public Guest getGuestById(Id guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El huésped con el ID '" + guestId + "' no existe."));
    }

    @Override
    public Guest createNewGuest(Id id, Person person, String address) {
        return Guest.builder()
                .guestId(id)
                .person(person)
                .address(address)
                .build();
    }

    @Override
    public void createAndSaveGuest(Id id, Person person, String address) {
        Guest guest = createNewGuest(id, person, address);
        guestRepository.save(guest);
    }

    @Override
    @Transactional
    public Response updateGuest(GuestUpdateRequest request) {
        Id guestId = buildId(request.getGuestToBeUpdated().getType(), request.getGuestToBeUpdated().getNumber());
        Guest guest = getGuestById(guestId);

        guest.setAddress(request.getGuestExtraInformation().getAddress());

        return generateResponse(OK, GUEST_UPDATED_SUCCESSFULLY);
    }

    @Override
    public Response getGuestToResponseById(IdRequest request) {
        Id guestId = buildId(request.getType(), request.getNumber());

        Guest guest = guestRepository.findByIdWithPerson(guestId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "Huésped no encontrado"));

        GuestResponse guestResponse = guestMapper.guestToResponse(guest);
        return generateResponse(guestResponse, OK, GUEST_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public Response getAllGuests(PageableRequest request) {
        paginationValidator.validatePagination(request.getPageNumber(), request.getPageSize());
        Pageable pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                Sort.by(Sort.Direction.ASC, "guestId")
        );

        Page<Guest> guestsPage = guestRepository.findAllWithPersonAndDocument(pageable);
        Page<GuestResponse> guestResponses = guestsPage.map(guestMapper::guestToResponse);
        PagedResponse<GuestResponse> response = pagedResponseMapper.toPagedResponse(guestResponses);
        return generateResponse(response, OK, GUEST_RETRIEVED_SUCCESSFULLY);
    }

}