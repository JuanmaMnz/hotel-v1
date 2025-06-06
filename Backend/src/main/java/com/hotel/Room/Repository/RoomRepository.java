package com.hotel.Room.Repository;

import com.hotel.Room.Model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {

    boolean existsByNumber(int number);

    @EntityGraph(attributePaths = {"imagesUrls"})
    Optional<Room> findById(Integer roomId);

    @EntityGraph(attributePaths = {"imagesUrls"})
    Page<Room> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"imagesUrls"})
    Page<Room> findAll(@Nullable Specification<Room> spec, Pageable pageable);
}