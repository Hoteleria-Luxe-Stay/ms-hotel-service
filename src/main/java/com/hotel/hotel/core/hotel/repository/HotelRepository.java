package com.hotel.hotel.core.hotel.repository;

import com.hotel.hotel.core.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByDepartamentoId(Long departamentoId);
}
