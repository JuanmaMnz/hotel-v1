package com.hotel.RoomItem.Repository;

import com.hotel.RoomItem.Model.RoomItem;
import com.hotel.RoomItem.Model.RoomItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoomItemRepository extends JpaRepository<RoomItem, RoomItemId> {

    Set<RoomItem> findAllByRoomItemIdRoomId(Integer roomId);
}