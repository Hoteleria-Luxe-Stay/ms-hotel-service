package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DisponibilidadResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T10:30:14.501746300-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class DisponibilidadResponse {

  private Long habitacionId;

  private Boolean disponible;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaInicio;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fechaFin;

  public DisponibilidadResponse habitacionId(Long habitacionId) {
    this.habitacionId = habitacionId;
    return this;
  }

  /**
   * Get habitacionId
   * @return habitacionId
  */
  
  @Schema(name = "habitacionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("habitacionId")
  public Long getHabitacionId() {
    return habitacionId;
  }

  public void setHabitacionId(Long habitacionId) {
    this.habitacionId = habitacionId;
  }

  public DisponibilidadResponse disponible(Boolean disponible) {
    this.disponible = disponible;
    return this;
  }

  /**
   * Get disponible
   * @return disponible
  */
  
  @Schema(name = "disponible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disponible")
  public Boolean getDisponible() {
    return disponible;
  }

  public void setDisponible(Boolean disponible) {
    this.disponible = disponible;
  }

  public DisponibilidadResponse fechaInicio(LocalDate fechaInicio) {
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

  public DisponibilidadResponse fechaFin(LocalDate fechaFin) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisponibilidadResponse disponibilidadResponse = (DisponibilidadResponse) o;
    return Objects.equals(this.habitacionId, disponibilidadResponse.habitacionId) &&
        Objects.equals(this.disponible, disponibilidadResponse.disponible) &&
        Objects.equals(this.fechaInicio, disponibilidadResponse.fechaInicio) &&
        Objects.equals(this.fechaFin, disponibilidadResponse.fechaFin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(habitacionId, disponible, fechaInicio, fechaFin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisponibilidadResponse {\n");
    sb.append("    habitacionId: ").append(toIndentedString(habitacionId)).append("\n");
    sb.append("    disponible: ").append(toIndentedString(disponible)).append("\n");
    sb.append("    fechaInicio: ").append(toIndentedString(fechaInicio)).append("\n");
    sb.append("    fechaFin: ").append(toIndentedString(fechaFin)).append("\n");
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

