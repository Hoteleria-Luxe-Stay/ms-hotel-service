package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.hotel.hotel.api.dto.TipoHabitacionResponse;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HabitacionResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T10:30:14.501746300-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HabitacionResponse {

  private Long id;

  private String numero;

  private Double precio;

  private Integer capacidad;

  private TipoHabitacionResponse tipoHabitacion;

  private Long hotelId;

  public HabitacionResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HabitacionResponse numero(String numero) {
    this.numero = numero;
    return this;
  }

  /**
   * Get numero
   * @return numero
  */
  
  @Schema(name = "numero", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("numero")
  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public HabitacionResponse precio(Double precio) {
    this.precio = precio;
    return this;
  }

  /**
   * Get precio
   * @return precio
  */
  
  @Schema(name = "precio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precio")
  public Double getPrecio() {
    return precio;
  }

  public void setPrecio(Double precio) {
    this.precio = precio;
  }

  public HabitacionResponse capacidad(Integer capacidad) {
    this.capacidad = capacidad;
    return this;
  }

  /**
   * Get capacidad
   * @return capacidad
  */
  
  @Schema(name = "capacidad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capacidad")
  public Integer getCapacidad() {
    return capacidad;
  }

  public void setCapacidad(Integer capacidad) {
    this.capacidad = capacidad;
  }

  public HabitacionResponse tipoHabitacion(TipoHabitacionResponse tipoHabitacion) {
    this.tipoHabitacion = tipoHabitacion;
    return this;
  }

  /**
   * Get tipoHabitacion
   * @return tipoHabitacion
  */
  @Valid 
  @Schema(name = "tipoHabitacion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tipoHabitacion")
  public TipoHabitacionResponse getTipoHabitacion() {
    return tipoHabitacion;
  }

  public void setTipoHabitacion(TipoHabitacionResponse tipoHabitacion) {
    this.tipoHabitacion = tipoHabitacion;
  }

  public HabitacionResponse hotelId(Long hotelId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HabitacionResponse habitacionResponse = (HabitacionResponse) o;
    return Objects.equals(this.id, habitacionResponse.id) &&
        Objects.equals(this.numero, habitacionResponse.numero) &&
        Objects.equals(this.precio, habitacionResponse.precio) &&
        Objects.equals(this.capacidad, habitacionResponse.capacidad) &&
        Objects.equals(this.tipoHabitacion, habitacionResponse.tipoHabitacion) &&
        Objects.equals(this.hotelId, habitacionResponse.hotelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, numero, precio, capacidad, tipoHabitacion, hotelId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HabitacionResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    precio: ").append(toIndentedString(precio)).append("\n");
    sb.append("    capacidad: ").append(toIndentedString(capacidad)).append("\n");
    sb.append("    tipoHabitacion: ").append(toIndentedString(tipoHabitacion)).append("\n");
    sb.append("    hotelId: ").append(toIndentedString(hotelId)).append("\n");
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

