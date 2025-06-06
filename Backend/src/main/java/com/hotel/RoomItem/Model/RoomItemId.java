package com.hotel.RoomItem.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomItemId {

    @Column(name = "room_id", nullable = false, updatable = false, unique = true)
    private Integer roomId;

    @Column(name = "item_id", nullable = false, updatable = false, unique = true)
    private Integer itemId;
}
