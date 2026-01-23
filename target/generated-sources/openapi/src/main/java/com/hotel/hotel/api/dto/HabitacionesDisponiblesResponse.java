package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.hotel.hotel.api.dto.HabitacionResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HabitacionesDisponiblesResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T03:40:40.575624500-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HabitacionesDisponiblesResponse {

  private Long hotelId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaInicio;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaFin;

  @Valid
  private List<@Valid HabitacionResponse> habitaciones = new ArrayList<>();

  public HabitacionesDisponiblesResponse hotelId(Long hotelId) {
    this.hotelId = hotelId;
    return this;
  }

  /**
   * Get hotelId
   * @return hotelId
  */
  
  @Schema(name = "hotelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hotelId")
  public Long getHotelId() {
    return hotelId;
  }

  public void setHotelId(Long hotelId) {
    this.hotelId = hotelId;
  }

  public HabitacionesDisponiblesResponse fechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
    return this;
  }

  /**
   * Get fechaInicio
   * @return fechaInicio
  */
  @Valid 
  @Schema(name = "fechaInicio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fechaInicio")
  public LocalDate getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public HabitacionesDisponiblesResponse fechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
    return this;
  }

  /**
   * Get fechaFin
   * @return fechaFin
  */
  @Valid 
  @Schema(name = "fechaFin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fechaFin")
  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public HabitacionesDisponiblesResponse habitaciones(List<@Valid HabitacionResponse> habitaciones) {
    this.habitaciones = habitaciones;
    return this;
  }

  public HabitacionesDisponiblesResponse addHabitacionesItem(HabitacionResponse habitacionesItem) {
    if (this.habitaciones == null) {
      this.habitaciones = new ArrayList<>();
    }
    this.habitaciones.add(habitacionesItem);
    return this;
  }

  /**
   * Get habitaciones
   * @return habitaciones
  */
  @Valid 
  @Schema(name = "habitaciones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("habitaciones")
  public List<@Valid HabitacionResponse> getHabitaciones() {
    return habitaciones;
  }

  public void setHabitaciones(List<@Valid HabitacionResponse> habitaciones) {
    this.habitaciones = habitaciones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HabitacionesDisponiblesResponse habitacionesDisponiblesResponse = (HabitacionesDisponiblesResponse) o;
    return Objects.equals(this.hotelId, habitacionesDisponiblesResponse.hotelId) &&
        Objects.equals(this.fechaInicio, habitacionesDisponiblesResponse.fechaInicio) &&
        Objects.equals(this.fechaFin, habitacionesDisponiblesResponse.fechaFin) &&
        Objects.equals(this.habitaciones, habitacionesDisponiblesResponse.habitaciones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hotelId, fechaInicio, fechaFin, habitaciones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HabitacionesDisponiblesResponse {\n");
    sb.append("    hotelId: ").append(toIndentedString(hotelId)).append("\n");
    sb.append("    fechaInicio: ").append(toIndentedString(fechaInicio)).append("\n");
    sb.append("    fechaFin: ").append(toIndentedString(fechaFin)).append("\n");
    sb.append("    habitaciones: ").append(toIndentedString(habitaciones)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

