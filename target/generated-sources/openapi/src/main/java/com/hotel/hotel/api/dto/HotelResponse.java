package com.hotel.hotel.api.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.hotel.hotel.api.dto.DepartamentoResponse;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HotelResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T03:40:40.575624500-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HotelResponse {

  private Long id;

  private String nombre;

  private String direccion;

  private String descripcion;

  private String telefono;

  private String email;

  private DepartamentoResponse departamento;

  public HotelResponse id(Long id) {
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

  public HotelResponse nombre(String nombre) {
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

  public HotelResponse direccion(String direccion) {
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

  public HotelResponse descripcion(String descripcion) {
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

  public HotelResponse telefono(String telefono) {
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

  public HotelResponse email(String email) {
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

  public HotelResponse departamento(DepartamentoResponse departamento) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HotelResponse hotelResponse = (HotelResponse) o;
    return Objects.equals(this.id, hotelResponse.id) &&
        Objects.equals(this.nombre, hotelResponse.nombre) &&
        Objects.equals(this.direccion, hotelResponse.direccion) &&
        Objects.equals(this.descripcion, hotelResponse.descripcion) &&
        Objects.equals(this.telefono, hotelResponse.telefono) &&
        Objects.equals(this.email, hotelResponse.email) &&
        Objects.equals(this.departamento, hotelResponse.departamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, direccion, descripcion, telefono, email, departamento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HotelResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    direccion: ").append(toIndentedString(direccion)).append("\n");
    sb.append("    descripcion: ").append(toIndentedString(descripcion)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    departamento: ").append(toIndentedString(departamento)).append("\n");
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

