package com.hotel.RoomItem.Validator;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Item.Model.Item;
import com.hotel.Item.Service.IItemService;
import com.hotel.RoomItem.DTO.RoomItemRequest;
import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Model.RoomItemId;
import com.hotel.RoomItem.Repository.RoomItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;
import static com.hotel.Common.Exception.ErrorCode.RESOURCE_CONFLICT;
import static com.hotel.RoomItem.Utils.RoomItemUtils.buildRoomItemId;

@Component
@RequiredArgsConstructor
public class RoomItemValidator {

    private final IItemService itemService;
    private final RoomItemRepository roomItemRepository;

    public void validateRoomItemRequest(Integer roomId, Integer itemId, RoomItemRequest request) {
        if (request.getQuantity() <= 0) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La cantidad del artículo debe ser mayor que 0");
        }
        validateAvailableStock(roomId, itemId, request.getQuantity());
    }

    public void validateAvailableStock(Integer roomId, Integer itemId, int quantityToUpdate) {
        Item item = itemService.getItemById(itemId);
        RoomItemId roomItemId = buildRoomItemId(itemId, roomId);

        Optional<RoomItem> existingRoomItem = roomItemRepository.findById(roomItemId);

        if (existingRoomItem.isPresent()) {
            int maxAssignable = item.getTotalAvailableQuantity() + existingRoomItem.get().getQuantity();

            if (quantityToUpdate > maxAssignable) {
                throw new InvalidRequestException(RESOURCE_CONFLICT,
                        "No hay suficiente cantidad del articulo disponible para asignar a la habitación");
            }
        } else {
            if (quantityToUpdate > item.getTotalAvailableQuantity()) {
                throw new InvalidRequestException(RESOURCE_CONFLICT,
                        "No hay suficiente cantidad del articulo disponible para asignar a la habitación");
            }
        }
    }

}