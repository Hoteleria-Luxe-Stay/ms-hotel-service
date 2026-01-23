package com.hotel.hotel.core.hotel.model;

import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String descripcion;
    private String telefono;
    private String email;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    public double getPrecioMinimo() {
        if (habitaciones == null || habitaciones.isEmpty()) {
            return 0.0;
        }

        return habitaciones.stream()
                .mapToDouble(Habitacion::getPrecio)
                .min()
                .orElse(0.0);
    }

    public double getPrecioMaximo() {
        if (habitaciones == null || habitaciones.isEmpty()) {
            return 0.0;
        }

        return habitaciones.stream()
                .mapToDouble(Habitacion::getPrecio)
                .max()
                .orElse(0.0);
    }
}
