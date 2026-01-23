package com.hotel.hotel.core.habitacion.repository;

import com.hotel.hotel.core.habitacion.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByHotelId(Long hotelId);

    List<Habitacion> findByHotelIdAndEstado(Long hotelId, String estado);

    long countByHotelIdAndEstado(Long hotelId, String estado);
}
