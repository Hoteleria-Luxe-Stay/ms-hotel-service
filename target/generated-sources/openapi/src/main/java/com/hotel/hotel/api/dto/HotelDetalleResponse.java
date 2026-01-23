package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.hotel.hotel.api.dto.DepartamentoResponse;
import com.hotel.hotel.api.dto.HabitacionResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HotelDetalleResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T03:40:40.575624500-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HotelDetalleResponse {

  private Long id;

  private String nombre;

  private String direccion;

  private String descripcion;

  private String telefono;

  private String email;

  private DepartamentoResponse departamento;

  @Valid
  private List<@Valid HabitacionResponse> habitaciones = new ArrayList<>();

  private Double precioMinimo;

  private Double precioMaximo;

  public HotelDetalleResponse id(Long id) {
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

  public HotelDetalleResponse nombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  /**
   * Get nombre
   * @return nombre
  */
  
  @Schema(name = "nombre", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nombre")
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public HotelDetalleResponse direccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  /**
   * Get direccion
   * @return direccion
  */
  
  @Schema(name = "direccion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("direccion")
  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public HotelDetalleResponse descripcion(String descripcion) {
    this.descripcion = descripcion;
    return this;
  }

  /**
   * Get descripcion
   * @return descripcion
  */
  
  @Schema(name = "descripcion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("descripcion")
  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public HotelDetalleResponse telefono(String telefono) {
    this.telefono = telefono;
    return this;
  }

  /**
   * Get telefono
   * @return telefono
  */
  
  @Schema(name = "telefono", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telefono")
  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public HotelDetalleResponse email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public HotelDetalleResponse departamento(DepartamentoResponse departamento) {
    this.departamento = departamento;
    return this;
  }

  /**
   * Get departamento
   * @return departamento
  */
  @Valid 
  @Schema(name = "departamento", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departamento")
  public DepartamentoResponse getDepartamento() {
    return departamento;
  }

  public void setDepartamento(DepartamentoResponse departamento) {
    this.departamento = departamento;
  }

  public HotelDetalleResponse habitaciones(List<@Valid HabitacionResponse> habitaciones) {
    this.habitaciones = habitaciones;
    return this;
  }

  public HotelDetalleResponse addHabitacionesItem(HabitacionResponse habitacionesItem) {
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

  public HotelDetalleResponse precioMinimo(Double precioMinimo) {
    this.precioMinimo = precioMinimo;
    return this;
  }

  /**
   * Get precioMinimo
   * @return precioMinimo
  */
  
  @Schema(name = "precioMinimo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precioMinimo")
  public Double getPrecioMinimo() {
    return precioMinimo;
  }

  public void setPrecioMinimo(Double precioMinimo) {
    this.precioMinimo = precioMinimo;
  }

  public HotelDetalleResponse precioMaximo(Double precioMaximo) {
    this.precioMaximo = precioMaximo;
    return this;
  }

  /**
   * Get precioMaximo
   * @return precioMaximo
  */
  
  @Schema(name = "precioMaximo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("precioMaximo")
  public Double getPrecioMaximo() {
    return precioMaximo;
  }

  public void setPrecioMaximo(Double precioMaximo) {
    this.precioMaximo = precioMaximo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HotelDetalleResponse hotelDetalleResponse = (HotelDetalleResponse) o;
    return Objects.equals(this.id, hotelDetalleResponse.id) &&
        Objects.equals(this.nombre, hotelDetalleResponse.nombre) &&
        Objects.equals(this.direccion, hotelDetalleResponse.direccion) &&
        Objects.equals(this.descripcion, hotelDetalleResponse.descripcion) &&
        Objects.equals(this.telefono, hotelDetalleResponse.telefono) &&
        Objects.equals(this.email, hotelDetalleResponse.email) &&
        Objects.equals(this.departamento, hotelDetalleResponse.departamento) &&
        Objects.equals(this.habitaciones, hotelDetalleResponse.habitaciones) &&
        Objects.equals(this.precioMinimo, hotelDetalleResponse.precioMinimo) &&
        Objects.equals(this.precioMaximo, hotelDetalleResponse.precioMaximo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, direccion, descripcion, telefono, email, departamento, habitaciones, precioMinimo, precioMaximo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HotelDetalleResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    direccion: ").append(toIndentedString(direccion)).append("\n");
    sb.append("    descripcion: ").append(toIndentedString(descripcion)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    departamento: ").append(toIndentedString(departamento)).append("\n");
    sb.append("    habitaciones: ").append(toIndentedString(habitaciones)).append("\n");
    sb.append("    precioMinimo: ").append(toIndentedString(precioMinimo)).append("\n");
    sb.append("    precioMaximo: ").append(toIndentedString(precioMaximo)).append("\n");
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

