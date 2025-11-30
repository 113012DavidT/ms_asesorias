package com.uteq.admin.service.impl;

import com.uteq.admin.dto.GrupoDTO;
import com.uteq.admin.entity.Grupo;
import com.uteq.admin.entity.ProfesorPerfil;
import com.uteq.admin.entity.Division;
import com.uteq.admin.entity.Programa;
import com.uteq.admin.repository.GrupoRepository;
import com.uteq.admin.repository.ProfesorPerfilRepository;
import com.uteq.admin.repository.DivisionRepository;
import com.uteq.admin.repository.ProgramaRepository;
import com.uteq.admin.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {
    private final GrupoRepository repository;
    private final ProfesorPerfilRepository profesorRepository;
    private final DivisionRepository divisionRepository;
    private final ProgramaRepository programaRepository;

    @Override
    public Grupo crear(GrupoDTO dto) {
        ProfesorPerfil profesor = profesorRepository.findById(dto.getProfesorId())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + dto.getProfesorId()));
        
        Division division = divisionRepository.findById(dto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " + dto.getDivisionId()));
        
        Programa programa = programaRepository.findById(dto.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no encontrado con ID: " + dto.getProgramaId()));

        Grupo grupo = Grupo.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .profesor(profesor)
                .division(division)
                .programa(programa)
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        return repository.save(grupo);
    }

    @Override
    public Grupo actualizar(Long id, GrupoDTO dto) {
        Grupo grupo = obtenerPorId(id);
        
        if (dto.getProfesorId() != null) {
            ProfesorPerfil profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
            grupo.setProfesor(profesor);
        }
        
        if (dto.getDivisionId() != null) {
            Division division = divisionRepository.findById(dto.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("División no encontrada"));
            grupo.setDivision(division);
        }
        
        if (dto.getProgramaId() != null) {
            Programa programa = programaRepository.findById(dto.getProgramaId())
                    .orElseThrow(() -> new RuntimeException("Programa no encontrado"));
            grupo.setPrograma(programa);
        }

        grupo.setNombre(dto.getNombre());
        grupo.setDescripcion(dto.getDescripcion());
        grupo.setFechaActualizacion(LocalDate.now());
        return repository.save(grupo);
    }

    @Override
    public Grupo obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + id));
    }

    @Override
    public List<Grupo> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Grupo> listarActivos() {
        return repository.findByActivo(true);
    }

    @Override
    public List<Grupo> listarPorProfesor(Long profesorId) {
        ProfesorPerfil profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        return repository.findByProfesor(profesor);
    }

    @Override
    public List<Grupo> listarPorDivision(Long divisionId) {
        Division division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("División no encontrada"));
        return repository.findByDivision(division);
    }

    @Override
    public List<Grupo> listarPorPrograma(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new RuntimeException("Programa no encontrado"));
        return repository.findByPrograma(programa);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void desactivar(Long id) {
        Grupo grupo = obtenerPorId(id);
        grupo.setActivo(false);
        grupo.setFechaActualizacion(LocalDate.now());
        repository.save(grupo);
    }
}
