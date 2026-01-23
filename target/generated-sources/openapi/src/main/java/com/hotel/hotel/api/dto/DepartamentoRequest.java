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
 * DepartamentoRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-23T10:30:14.501746300-05:00[America/Lima]", comments = "Generator version: 7.6.0")
public class DepartamentoRequest {

  private String nombre;

  public DepartamentoRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DepartamentoRequest(String nombre) {
    this.nombre = nombre;
  }

  public DepartamentoRequest nombre(String nombre) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DepartamentoRequest departamentoRequest = (DepartamentoRequest) o;
    return Objects.equals(this.nombre, departamentoRequest.nombre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DepartamentoRequest {\n");
    sb.append("    nombre: ").append(toIndentedString(nombre)).append("\n");
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

