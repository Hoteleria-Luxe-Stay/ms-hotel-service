package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HabitacionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T10:30:14.501746300-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HabitacionRequest {

  private String numero;

  private Double precio;

  private Integer capacidad;

  private Long hotelId;

  private Long tipoHabitacionId;

  public HabitacionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HabitacionRequest(String numero, Double precio, Integer capacidad, Long hotelId, Long tipoHabitacionId) {
    this.numero = numero;
    this.precio = precio;
    this.capacidad = capacidad;
    this.hotelId = hotelId;
    this.tipoHabitacionId = tipoHabitacionId;
  }

  public HabitacionRequest numero(String numero) {
    this.numero = numero;
    return this;
  }

  /**
   * Get numero
   * @return numero
  */
  @NotNull 
  @Schema(name = "numero", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("numero")
  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public HabitacionRequest precio(Double precio) {
    this.precio = precio;
    return this;
  }

  /**
   * Get precio
   * @return precio
  */
  @NotNull 
  @Schema(name = "precio", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("precio")
  public Double getPrecio() {
    return precio;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public HabitacionRequest capacidad(Integer capacidad) {
    this.capacidad = capacidad;
    return this;
  }

  /**
   * Get capacidad
   * @return capacidad
  */
  @NotNull 
  @Schema(name = "capacidad", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("capacidad")
  public Integer getCapacidad() {
    return capacidad;
  }

  public void setCapacidad(Integer capacidad) {
    this.capacidad = capacidad;
  }

  public HabitacionRequest hotelId(Long hotelId) {
    this.hotelId = hotelId;
    return this;
  }

  /**
   * Get hotelId
   * @return hotelId
  */
  @NotNull 
  @Schema(name = "hotelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hotelId")
  public Long getHotelId() {
    return hotelId;
  }

  public void setHotelId(Long hotelId) {
    this.hotelId = hotelId;
  }

  public HabitacionRequest tipoHabitacionId(Long tipoHabitacionId) {
    this.tipoHabitacionId = tipoHabitacionId;
    return this;
  }

  /**
   * Get tipoHabitacionId
   * @return tipoHabitacionId
  */
  @NotNull 
  @Schema(name = "tipoHabitacionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tipoHabitacionId")
  public Long getTipoHabitacionId() {
    return tipoHabitacionId;
  }

  public void setTipoHabitacionId(Long tipoHabitacionId) {
    this.tipoHabitacionId = tipoHabitacionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HabitacionRequest habitacionRequest = (HabitacionRequest) o;
    return Objects.equals(this.numero, habitacionRequest.numero) &&
        Objects.equals(this.precio, habitacionRequest.precio) &&
        Objects.equals(this.capacidad, habitacionRequest.capacidad) &&
        Objects.equals(this.hotelId, habitacionRequest.hotelId) &&
        Objects.equals(this.tipoHabitacionId, habitacionRequest.tipoHabitacionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero, precio, capacidad, hotelId, tipoHabitacionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HabitacionRequest {\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    precio: ").append(toIndentedString(precio)).append("\n");
    sb.append("    capacidad: ").append(toIndentedString(capacidad)).append("\n");
    sb.append("    hotelId: ").append(toIndentedString(hotelId)).append("\n");
    sb.append("    tipoHabitacionId: ").append(toIndentedString(tipoHabitacionId)).append("\n");
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

