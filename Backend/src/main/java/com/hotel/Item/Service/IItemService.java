package com.hotel.Item.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Item.DTO.ItemRequest;
import com.hotel.Item.Model.Item;

public interface IItemService {

    Item createNewItem(ItemRequest request);

    Response getAllItemsToResponse(PageableRequest pageable);

    Item getItemById(Integer itemId);

    Response createAndSaveItem(ItemRequest request);

    Response updateItem(Integer itemId, ItemRequest request);

    Response deleteItem(Integer itemId);
}