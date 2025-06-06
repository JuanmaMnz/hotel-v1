package com.hotel.Room.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Room.DTO.RoomRequest;
import com.hotel.Room.DTO.RoomSearchCriteria;
import com.hotel.Room.Model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IRoomService {

    Room getRoomById(Integer roomId);

    Room createNewRoom(RoomRequest request);

    BigDecimal calculateRoomTotalPrice(BigDecimal pricePerNight, LocalDateTime checkIn, LocalDateTime checkOut);

    Response getRoomToResponseById(Integer roomId);

    Response createAndSaveRoom(RoomRequest request);

    Response updateRoom(Integer roomId, RoomRequest request);

    Response deleteRoom(Integer roomId);

    Response searchRooms(RoomSearchCriteria criteria, PageableRequest pageable);

    Response uploadImagesToRoom(Integer roomId, List<MultipartFile> images);

    Response deleteImageFromRoom(Integer roomId, String imageUrl);

}