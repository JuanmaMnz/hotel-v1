package com.hotel.Room.Service;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Image.Service.IImageService;
import com.hotel.Image.model.Image;
import com.hotel.Item.Model.Item;
import com.hotel.Room.DTO.RoomRequest;
import com.hotel.Room.DTO.RoomResponse;
import com.hotel.Room.DTO.RoomSearchCriteria;
import com.hotel.Room.Mapper.RoomMapper;
import com.hotel.Room.Model.Room;
import com.hotel.Room.Repository.RoomRepository;
import com.hotel.Room.Validator.RoomValidator;
import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Repository.RoomItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;
import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Room.Messages.RoomResponseMessages.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{

    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;
    private final RoomMapper roomMapper;
    private final IImageService imageService;
    private final RoomSearchOrchestrator roomSearchOrchestrator;
    private final RoomItemRepository roomItemRepository;

    @Override
    @Transactional
    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "La habitación con ID '" + roomId + "' no fue encontrada"));
    }

    @Override
    public Room createNewRoom(RoomRequest request) {
        roomValidator.validateRequest(request);
        return Room.builder()
                .number(request.getNumber())
                .floor(request.getFloor())
                .type(request.getType())
                .pricePerNight(request.getPricePerNight())
                .imagesUrls(new HashSet<>())
                .build();
    }

    @Override
    public BigDecimal calculateRoomTotalPrice(BigDecimal pricePerNight, LocalDateTime checkIn, LocalDateTime checkOut) {
        long nights = Duration.between(checkIn.toLocalDate().atStartOfDay(), checkOut.toLocalDate().atStartOfDay()).toDays();
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }

    @Override
    @Transactional
    public Response getRoomToResponseById(Integer roomId) {
        Room room = getRoomById(roomId);
        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
        return generateResponse(roomResponse, OK, ROOM_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public Response createAndSaveRoom(RoomRequest request) {
        Room room = createNewRoom(request);
        Room createdRoom = roomRepository.save(room);
        return generateResponse(
                "RoomId: '" + createdRoom.getRoomId() + "'",
                CREATED,
                ROOM_REGISTERED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateRoom(Integer roomId, RoomRequest request) {
        Room room = getRoomById(roomId);
        roomValidator.validateUpdateRequest(request, room);
        room.setNumber(request.getNumber());
        room.setFloor(request.getFloor());
        room.setType(request.getType());
        room.setPricePerNight(request.getPricePerNight());
        return generateResponse(OK, ROOM_UPDATED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response deleteRoom(Integer roomId) {
        Room room = getRoomById(roomId);

        imageService.deleteAllImages(roomId);
        if (room.getImagesUrls() != null) {
            room.getImagesUrls().clear();
        }

        Set<RoomItem> roomItems = roomItemRepository.findAllByRoomItemIdRoomId(roomId);
        roomItems.forEach(roomItem -> {
            Item item = roomItem.getItem();
            item.setTotalAvailableQuantity(item.getTotalAvailableQuantity() + roomItem.getQuantity());
        });
        roomItemRepository.deleteAll(roomItems);

        roomRepository.deleteById(roomId);
        return generateResponse(OK, ROOM_DELETED_SUCCESSFULLY);
    }

    @Override
    @Transactional(readOnly = true)
    public Response searchRooms(RoomSearchCriteria criteria, PageableRequest pageableRequest) {
        return roomSearchOrchestrator.searchRooms(criteria, pageableRequest);
    }

    @Override
    @Transactional
    public Response uploadImagesToRoom(Integer roomId, List<MultipartFile> images) {
        Room room = getRoomById(roomId);
        imageService.processAndSaveImages(images, room);
        return generateResponse(CREATED, IMAGE_UPLOADED_TO_ROOM);
    }

    @Override
    @Transactional
    public Response deleteImageFromRoom(Integer roomId, String imageUrl) {
        Room room = getRoomById(roomId);
        Image image = imageService.getImageByUrl(imageUrl);

        if (!image.getRoom().getRoomId().equals(room.getRoomId())) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La imagen no pertenece a la habitacion con el ID '" + roomId + "'");
        }

        room.getImagesUrls().remove(imageUrl);

        imageService.deleteImage(image.getImageUrl());
        return generateResponse(OK, IMAGE_DELETED_FROM_ROOM);
    }

}