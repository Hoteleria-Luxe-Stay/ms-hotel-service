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
 * HotelRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T10:30:14.501746300-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class HotelRequest {

  private String nombre;

  private String direccion;

  private String descripcion;

  private String telefono;

  private String email;

  private String imagenUrl;

  private Long departamentoId;

  public HotelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HotelRequest(String nombre, String direccion, Long departamentoId) {
    this.nombre = nombre;
    this.direccion = direccion;
    this.departamentoId = departamentoId;
  }

  public HotelRequest nombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  /**
   * Get nombre
   * @return nombre
  */
  @NotNull @Size(max = 100) 
  @Schema(name = "nombre", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nombre")
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public HotelRequest direccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  /**
   * Get direccion
   * @return direccion
  */
  @NotNull @Size(max = 200) 
  @Schema(name = "direccion", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("direccion")
  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public HotelRequest descripcion(String descripcion) {
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

  public HotelRequest telefono(String telefono) {
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

  public HotelRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public HotelRequest imagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
    return this;
  }

  /**
   * URL de la imagen del hotel
   * @return imagenUrl
  */
  
  @Schema(name = "imagenUrl", description = "URL de la imagen del hotel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("imagenUrl")
  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
  }

  public HotelRequest departamentoId(Long departamentoId) {
    this.departamentoId = departamentoId;
    return this;
  }

  /**
   * Get departamentoId
   * @return departamentoId
  */
  @NotNull 
  @Schema(name = "departamentoId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("departamentoId")
  public Long getDepartamentoId() {
    return departamentoId;
  }

  public void setDepartamentoId(Long departamentoId) {
    this.departamentoId = departamentoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HotelRequest hotelRequest = (HotelRequest) o;
    return Objects.equals(this.nombre, hotelRequest.nombre) &&
        Objects.equals(this.direccion, hotelRequest.direccion) &&
        Objects.equals(this.descripcion, hotelRequest.descripcion) &&
        Objects.equals(this.telefono, hotelRequest.telefono) &&
        Objects.equals(this.email, hotelRequest.email) &&
        Objects.equals(this.imagenUrl, hotelRequest.imagenUrl) &&
        Objects.equals(this.departamentoId, hotelRequest.departamentoId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre, direccion, descripcion, telefono, email, imagenUrl, departamentoId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HotelRequest {\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
    sb.append("    direccion: ").append(toIndentedString(direccion)).append("\n");
    sb.append("    descripcion: ").append(toIndentedString(descripcion)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    imagenUrl: ").append(toIndentedString(imagenUrl)).append("\n");
    sb.append("    departamentoId: ").append(toIndentedString(departamentoId)).append("\n");
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

