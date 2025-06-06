package com.hotel.RoomItem.Service;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Response.Response;
import com.hotel.Item.Model.Item;
import com.hotel.Item.Repository.ItemRepository;
import com.hotel.Item.Service.IItemService;
import com.hotel.Room.Model.Room;
import com.hotel.Room.Service.IRoomService;
import com.hotel.RoomItem.DTO.RoomItemRequest;
import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Model.RoomItemId;
import com.hotel.RoomItem.Repository.RoomItemRepository;
import com.hotel.RoomItem.Validator.RoomItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;
import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.RoomItem.Messages.RoomItemResponseMessages.*;
import static com.hotel.RoomItem.Utils.RoomItemUtils.buildRoomItemId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class RoomItemServiceImpl implements IRoomItemService{

    private final RoomItemRepository roomItemRepository;
    private final RoomItemValidator roomItemValidator;
    private final IRoomService roomService;
    private final IItemService itemService;
    private final ItemRepository itemRepository;

    @Override
    public RoomItem getRoomItemById(RoomItemId roomItemId) {
        return roomItemRepository.findById(roomItemId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No existe un artículo con el ID '" + roomItemId.getItemId() +
                                "' en la habitación con el ID '" + roomItemId.getRoomId() + "'"));
    }

    @Override
    public RoomItem createNewRoomItem(Integer roomId, Integer itemId, RoomItemRequest request) {
        Room room = roomService.getRoomById(roomId);
        Item item = itemService.getItemById(itemId);

        item.setTotalAvailableQuantity(item.getTotalAvailableQuantity() - request.getQuantity());
        itemRepository.save(item);

        RoomItemId roomItemId = buildRoomItemId(item.getItemId(), room.getRoomId());
        return RoomItem.builder()
                .roomItemId(roomItemId)
                .item(item)
                .room(room)
                .quantity(request.getQuantity())
                .build();
    }

    @Override
    @Transactional
    public Response assignItemToRoom(Integer roomId, Integer itemId, RoomItemRequest request) {
        roomItemValidator.validateRoomItemRequest(roomId, itemId, request);
        RoomItemId roomItemId = buildRoomItemId(itemId, roomId);

        if ( roomItemRepository.existsById(roomItemId) ) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "El item ya se encuentra asignado a la habitación. Si quieres cambiar la cantidad, usa el método 'updateItemInRoom'.");
        } else {
            RoomItem roomItem = createNewRoomItem(roomId, itemId, request);
            roomItemRepository.save(roomItem);
        }
        return generateResponse(CREATED, ITEM_ASSIGNED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateItemInRoom(Integer roomId, Integer itemId, RoomItemRequest request) {
        roomItemValidator.validateRoomItemRequest(roomId, itemId, request);
        RoomItemId roomItemId = buildRoomItemId(itemId, roomId);
        RoomItem existingRoomItem = getRoomItemById(roomItemId);
        Item item = itemService.getItemById(itemId);

        int oldQuantity = existingRoomItem.getQuantity();
        int newQuantity = request.getQuantity();
        int difference = oldQuantity - newQuantity;

        item.setTotalAvailableQuantity(item.getTotalAvailableQuantity() + difference);
        existingRoomItem.setQuantity(newQuantity);

        roomItemRepository.save(existingRoomItem);
        itemRepository.save(item);

        return generateResponse(OK, ITEM_UPDATED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response removeItemFromRoom(Integer roomId, Integer itemId) {
        RoomItemId roomItemId = buildRoomItemId(itemId, roomId);
        RoomItem existingRoomItem = getRoomItemById(roomItemId);

        Item item = itemService.getItemById(itemId);

        item.setTotalAvailableQuantity(item.getTotalAvailableQuantity() + existingRoomItem.getQuantity());
        itemRepository.save(item);
        roomItemRepository.delete(existingRoomItem);
        return generateResponse(OK, ITEM_REMOVED_SUCCESSFULLY);
    }

}