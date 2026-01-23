package com.hotel.hotel.core.departamento.service;

import com.hotel.hotel.api.dto.DepartamentoRequest;
import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.repository.DepartamentoRepository;
import com.hotel.hotel.helpers.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> listar() {
        return departamentoRepository.findAll();
    }

    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento", id));
    }

    public Optional<Departamento> buscarPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    public Departamento crear(DepartamentoRequest request) {
        Departamento departamento = new Departamento();
        departamento.setNombre(request.getNombre());
        return departamentoRepository.save(departamento);
    }

    public Departamento actualizar(Long id, DepartamentoRequest request) {
        Departamento departamento = buscarPorId(id);
        departamento.setNombre(request.getNombre());
        return departamentoRepository.save(departamento);
    }

    public void eliminar(Long depId) {
        if (!departamentoRepository.existsById(depId)) {
            throw new EntityNotFoundException("Departamento", depId);
        }
        departamentoRepository.deleteById(depId);
    }
}
