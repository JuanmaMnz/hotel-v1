package com.hotel.Room.Service;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Reservation.Repository.ReservationRepository;
import com.hotel.Room.DTO.RoomResponse;
import com.hotel.Room.DTO.RoomSearchCriteria;
import com.hotel.Room.Mapper.RoomMapper;
import com.hotel.Room.Model.Room;
import com.hotel.Room.Repository.RoomRepository;
import com.hotel.Room.Repository.RoomSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Room.Messages.RoomResponseMessages.ROOMS_RETRIEVED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@Component
@RequiredArgsConstructor
public class RoomSearchOrchestrator {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomMapper roomMapper;
    private final PagedResponseMapper pagedResponseMapper;
    private final PaginationValidator paginationValidator;

    @Transactional
    public Response searchRooms(RoomSearchCriteria criteria, PageableRequest pageableRequest) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        validateCriteria(criteria);

        List<Integer> unavailableRoomIds = new ArrayList<>();
        if (Boolean.TRUE.equals(criteria.getIsAvailable())) {
            unavailableRoomIds = reservationRepository
                    .findRoomIdsWithReservationsBetween(criteria.getCheckInDate(), criteria.getCheckOutDate());
        }

        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by("number").ascending()
        );

        Specification<Room> spec = RoomSpecifications.withFilters(criteria, unavailableRoomIds);
        Page<Room> roomPage = roomRepository.findAll(spec, pageable);
        Page<RoomResponse> response = roomPage.map(roomMapper::toRoomResponse);
        return generateResponse(pagedResponseMapper.toPagedResponse(response), OK, ROOMS_RETRIEVED_SUCCESSFULLY);
    }

    private void validateCriteria(RoomSearchCriteria criteria) {
        if (Boolean.TRUE.equals(criteria.getIsAvailable())) {
            if (criteria.getCheckInDate() == null || criteria.getCheckOutDate() == null) {
                throw new InvalidRequestException(INVALID_REQUEST,
                        "Para buscar habitaciones disponibles debes enviar checkInDate y checkOutDate.");
            }
        }
    }
}

