package com.hotel.RoomItem.Utils;

import com.hotel.RoomItem.Model.RoomItemId;

public class RoomItemUtils {

    public static RoomItemId buildRoomItemId(Integer itemId, Integer roomId){
        return RoomItemId.builder()
                .itemId(itemId)
                .roomId(roomId)
                .build();
    }

}