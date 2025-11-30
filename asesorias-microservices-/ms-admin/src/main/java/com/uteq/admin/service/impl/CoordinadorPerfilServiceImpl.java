package com.uteq.admin.service.impl;

import com.uteq.admin.dto.CoordinadorPerfilDTO;
import com.uteq.admin.entity.CoordinadorPerfil;
import com.uteq.admin.entity.Division;
import com.uteq.admin.entity.Programa;
import com.uteq.admin.repository.CoordinadorPerfilRepository;
import com.uteq.admin.repository.DivisionRepository;
import com.uteq.admin.repository.ProgramaRepository;
import com.uteq.admin.service.CoordinadorPerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoordinadorPerfilServiceImpl implements CoordinadorPerfilService {
    private final CoordinadorPerfilRepository repository;
    private final DivisionRepository divisionRepository;
    private final ProgramaRepository programaRepository;

    @Override
    public CoordinadorPerfil crear(CoordinadorPerfilDTO dto) {
        Division division = divisionRepository.findById(dto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("División no encontrada con ID: " + dto.getDivisionId()));
        
        Programa programa = programaRepository.findById(dto.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no encontrado con ID: " + dto.getProgramaId()));

        CoordinadorPerfil coordinador = CoordinadorPerfil.builder()
                .usuarioId(dto.getUsuarioId())
                .division(division)
                .programa(programa)
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        return repository.save(coordinador);
    }

    @Override
    public CoordinadorPerfil actualizar(Long id, CoordinadorPerfilDTO dto) {
        CoordinadorPerfil coordinador = obtenerPorId(id);
        
        if (dto.getDivisionId() != null) {
            Division division = divisionRepository.findById(dto.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("División no encontrada"));
            coordinador.setDivision(division);
        }
        
        if (dto.getProgramaId() != null) {
            Programa programa = programaRepository.findById(dto.getProgramaId())
                    .orElseThrow(() -> new RuntimeException("Programa no encontrado"));
            coordinador.setPrograma(programa);
        }

        coordinador.setFechaActualizacion(LocalDate.now());
        return repository.save(coordinador);
    }

    @Override
    public CoordinadorPerfil obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado con ID: " + id));
    }

    @Override
    public Optional<CoordinadorPerfil> obtenerPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<CoordinadorPerfil> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<CoordinadorPerfil> listarActivos() {
        return repository.findByActivo(true);
    }

    @Override
    public List<CoordinadorPerfil> listarPorDivision(Long divisionId) {
        Division division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("División no encontrada"));
        return repository.findByDivision(division);
    }

    @Override
    public List<CoordinadorPerfil> listarPorPrograma(Long programaId) {
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
        CoordinadorPerfil coordinador = obtenerPorId(id);
        coordinador.setActivo(false);
        coordinador.setFechaActualizacion(LocalDate.now());
        repository.save(coordinador);
    }
}
