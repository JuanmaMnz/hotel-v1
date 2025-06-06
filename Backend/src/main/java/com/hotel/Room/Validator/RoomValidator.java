package com.hotel.Room.Validator;

import com.hotel.Common.Exception.CommonExceptions.DuplicateResourceException;
import com.hotel.Room.DTO.RoomRequest;
import com.hotel.Room.Model.Room;
import com.hotel.Room.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.DUPLICATE_RESOURCE;

@Component
@RequiredArgsConstructor
public class RoomValidator {

    private final RoomRepository roomRepository;

    public void validateRequest(RoomRequest request) {
        validateUniqueRoomNumber(request.getNumber());
    }

    public void validateUpdateRequest(RoomRequest request, Room room) {
        validateUniqueRoomNumber(request.getNumber(), room);
    }

    void validateUniqueRoomNumber(int roomNumber, Room room){
        if ( roomRepository.existsByNumber(roomNumber) && (room.getNumber() != roomNumber) ){
            throw new DuplicateResourceException(DUPLICATE_RESOURCE,
                    "Ya existe una habitación registrada con el número '" + roomNumber + "'.");
        }
    }

    void validateUniqueRoomNumber(int roomNumber){
        if ( roomRepository.existsByNumber(roomNumber) ){
            throw new DuplicateResourceException(DUPLICATE_RESOURCE,
                    "Ya existe una habitación registrada con el número '" + roomNumber + "'.");
        }
    }

}