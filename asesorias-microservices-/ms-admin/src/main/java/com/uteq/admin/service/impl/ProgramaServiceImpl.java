package com.uteq.admin.service.impl;

import com.uteq.admin.dto.ProgramaDTO;
import com.uteq.admin.entity.Programa;
import com.uteq.admin.repository.ProgramaRepository;
import com.uteq.admin.service.ProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {
    private final ProgramaRepository repository;

    @Override
    public Programa crear(ProgramaDTO dto) {
        Programa programa = Programa.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        return repository.save(programa);
    }

    @Override
    public Programa actualizar(Long id, ProgramaDTO dto) {
        Programa programa = obtenerPorId(id);
        programa.setNombre(dto.getNombre());
        programa.setDescripcion(dto.getDescripcion());
        programa.setFechaActualizacion(LocalDate.now());
        return repository.save(programa);
    }

    @Override
    public Programa obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programa no encontrado con ID: " + id));
    }

    @Override
    public List<Programa> listarTodas() {
        return repository.findAll();
    }

    @Override
    public List<Programa> listarActivos() {
        return repository.findByActivo(true);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void desactivar(Long id) {
        Programa programa = obtenerPorId(id);
        programa.setActivo(false);
        programa.setFechaActualizacion(LocalDate.now());
        repository.save(programa);
    }
}
