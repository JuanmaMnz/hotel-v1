package com.hotel.Image.Repository;

import com.hotel.Image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findByImageUrl(String imageUrl);

    Set<Image> findAllByRoom_RoomId(Integer roomId);

}