package com.hotel.hotel.core.tipoHabitacion.repository;

import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {

    Optional<TipoHabitacion> findByNombre(String nombre);
}
