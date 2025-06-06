package com.hotel.Room.Repository;


import com.hotel.Room.DTO.RoomSearchCriteria;
import com.hotel.Room.Model.Room;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoomSpecifications {

    public static Specification<Room> withFilters(RoomSearchCriteria criteria, List<Integer> unavailableRoomIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getMinPricePerNight() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerNight"), criteria.getMinPricePerNight()));
            }

            if (criteria.getMaxPricePerNight() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerNight"), criteria.getMaxPricePerNight()));
            }

            if (criteria.getType() != null) {
                predicates.add(cb.equal(cb.lower(root.get("type")), criteria.getType().toLowerCase()));
            }

            if (criteria.getFloor() != null) {
                predicates.add(cb.equal(root.get("floor"), criteria.getFloor()));
            }

            if (Boolean.TRUE.equals(criteria.getIsAvailable()) && !unavailableRoomIds.isEmpty()) {
                predicates.add(cb.not(root.get("roomId").in(unavailableRoomIds)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
