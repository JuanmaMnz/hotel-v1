package com.hotel.Room.Mapper;

import com.hotel.Room.DTO.RoomResponse;
import com.hotel.Room.Model.Room;
import com.hotel.RoomItem.DTO.RoomItemResponse;
import com.hotel.RoomItem.Mapper.RoomItemMapper;
import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Repository.RoomItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final RoomItemRepository roomItemRepository;
    private final RoomItemMapper roomItemMapper;


    public RoomResponse toRoomResponse(Room room) {
        Set<RoomItem> roomItemSet = roomItemRepository.findAllByRoomItemIdRoomId(room.getRoomId());
        Set<RoomItemResponse> roomItemResponses = roomItemMapper.toRoomItemResponseSet(roomItemSet);
        return new RoomResponse(
                room.getRoomId(),
                room.getNumber(),
                room.getType(),
                room.getFloor(),
                room.getPricePerNight(),
                roomItemResponses,
                room.getImagesUrls()
        );
    }

}