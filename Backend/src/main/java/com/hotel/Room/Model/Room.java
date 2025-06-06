package com.hotel.Room.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "floor", nullable = false)
    private int floor;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @ElementCollection
    @CollectionTable(name = "room_image_urls", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private Set<String> imagesUrls;

}