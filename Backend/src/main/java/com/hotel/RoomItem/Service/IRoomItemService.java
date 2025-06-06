package com.hotel.RoomItem.Service;

import com.hotel.Common.Response.Response;
import com.hotel.RoomItem.DTO.RoomItemRequest;
import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Model.RoomItemId;

public interface IRoomItemService {

    RoomItem getRoomItemById(RoomItemId roomItemId);

    RoomItem createNewRoomItem(Integer roomId, Integer itemId, RoomItemRequest request);

    Response assignItemToRoom(Integer roomId, Integer itemId, RoomItemRequest request);

    Response updateItemInRoom(Integer roomId, Integer itemId, RoomItemRequest request);

    Response removeItemFromRoom(Integer roomId, Integer itemId);
}