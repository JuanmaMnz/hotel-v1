package com.hotel.HotelServices.Repository;

import com.hotel.HotelServices.Model.HotelServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelServicesRepository extends JpaRepository<HotelServices, Integer> {
}
