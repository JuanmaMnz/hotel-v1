package com.hotel.Room.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchCriteria {

    private Boolean isAvailable;

    private LocalDateTime checkInDate;

    private LocalDateTime checkOutDate;

    private BigDecimal minPricePerNight;

    private BigDecimal maxPricePerNight;

    private String type;

    private Integer floor;

}