package com.hotel.RoomItem.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotel.Item.Model.Item;
import com.hotel.Room.Model.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomItem {

    @EmbeddedId
    private RoomItemId roomItemId;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private Item item;

    @Column(name = "quantity")
    private int quantity;

}