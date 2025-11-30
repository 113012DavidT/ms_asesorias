package com.uteq.profesores.service;

import com.uteq.profesores.dto.ProfesorDTO;
import com.uteq.profesores.entity.Profesor;
import com.uteq.profesores.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepo;

    @Override
    @Transactional
    public Profesor crear(Profesor p) {
        return profesorRepo.save(p);
    }

    @Override
    public List<ProfesorDTO> listar() {
        return profesorRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Profesor obtener(Long id) {
        return profesorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
    }

    @Override
    public Profesor obtenerPorUsuario(Long usuarioId) {
        return profesorRepo.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public Profesor actualizar(Long id, Profesor p) {
        Profesor profesor = obtener(id);
        if (p.getDivisionId() != null) profesor.setDivisionId(p.getDivisionId());
        if (p.getProgramaId() != null) profesor.setProgramaId(p.getProgramaId());
        if (p.getNombre() != null) profesor.setNombre(p.getNombre());
        if (p.getEspecialidad() != null) profesor.setEspecialidad(p.getEspecialidad());
        if (p.getActivo() != null) profesor.setActivo(p.getActivo());
        return profesorRepo.save(profesor);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        profesorRepo.deleteById(id);
    }

    @Override
    public List<Profesor> listarPorDivisionYPrograma(Long divisionId, Long programaId) {
        return profesorRepo.findByDivisionIdAndProgramaId(divisionId, programaId);
    }

    private ProfesorDTO toDTO(Profesor p) {
        return ProfesorDTO.builder()
                .id(p.getId())
                .usuarioId(p.getUsuarioId())
                .divisionId(p.getDivisionId())
                .programaId(p.getProgramaId())
                .nombre(p.getNombre())
                .correoMatricula(p.getCorreoMatricula())
                .especialidad(p.getEspecialidad())
                .activo(p.getActivo())
                .build();
    }
}
