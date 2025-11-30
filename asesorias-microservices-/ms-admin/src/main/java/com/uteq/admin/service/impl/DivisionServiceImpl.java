package com.uteq.admin.service.impl;

import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.entity.Division;
import com.uteq.admin.repository.DivisionRepository;
import com.uteq.admin.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {
    private final DivisionRepository repository;

    @Override
    public Division crear(DivisionDTO dto) {
        Division division = Division.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        return repository.save(division);
    }

    @Override
    public Division actualizar(Long id, DivisionDTO dto) {
        Division division = obtenerPorId(id);
        division.setNombre(dto.getNombre());
        division.setDescripcion(dto.getDescripcion());
        division.setFechaActualizacion(LocalDate.now());
        return repository.save(division);
    }

    @Override
    public Division obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " + id));
    }

    @Override
    public List<Division> listarTodas() {
        return repository.findAll();
    }

    @Override
    public List<Division> listarActivas() {
        return repository.findByActivo(true);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void desactivar(Long id) {
        Division division = obtenerPorId(id);
        division.setActivo(false);
        division.setFechaActualizacion(LocalDate.now());
        repository.save(division);
    }
}
