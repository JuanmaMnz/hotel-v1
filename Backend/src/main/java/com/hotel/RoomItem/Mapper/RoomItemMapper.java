package com.hotel.RoomItem.Mapper;

import com.hotel.RoomItem.DTO.RoomItemResponse;
import com.hotel.RoomItem.Model.RoomItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoomItemMapper {

    @Mapping(source = "item.itemId", target = "id")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.description", target = "itemDescription")
    @Mapping(source = "quantity", target = "itemQuantity")
    RoomItemResponse toRoomItemResponse(RoomItem roomItem);

    Set<RoomItemResponse> toRoomItemResponseSet(Set<RoomItem> roomItems);
}