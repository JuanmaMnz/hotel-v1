package com.hotel.Item.Mapper;

import com.hotel.Item.DTO.ItemResponse;
import com.hotel.Item.Model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "itemId", target = "id")
    @Mapping(source = "totalAvailableQuantity", target = "totalAvailableQuantity")
    ItemResponse toItemResponse(Item item);
}