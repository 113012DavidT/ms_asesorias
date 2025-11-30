package com.uteq.alumnos.service;

import com.uteq.alumnos.dto.AlumnoDTO;
import com.uteq.alumnos.entity.Alumno;
import com.uteq.alumnos.repository.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepo;

    @Override
    @Transactional
    public Alumno crear(Alumno a) {
        return alumnoRepo.save(a);
    }

    @Override
    public List<AlumnoDTO> listar() {
        return alumnoRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Alumno obtener(Long id) {
        return alumnoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    }

    @Override
    public Alumno obtenerPorUsuario(Long usuarioId) {
        return alumnoRepo.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public Alumno actualizar(Long id, Alumno a) {
        Alumno alumno = obtener(id);
        if (a.getDivisionId() != null) alumno.setDivisionId(a.getDivisionId());
        if (a.getProgramaId() != null) alumno.setProgramaId(a.getProgramaId());
        if (a.getNombre() != null) alumno.setNombre(a.getNombre());
        if (a.getActivo() != null) alumno.setActivo(a.getActivo());
        return alumnoRepo.save(alumno);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        alumnoRepo.deleteById(id);
    }

    @Override
    public List<Alumno> listarPorDivisionYPrograma(Long divisionId, Long programaId) {
        return alumnoRepo.findByDivisionIdAndProgramaId(divisionId, programaId);
    }

    private AlumnoDTO toDTO(Alumno a) {
        return AlumnoDTO.builder()
                .id(a.getId())
                .usuarioId(a.getUsuarioId())
                .divisionId(a.getDivisionId())
                .programaId(a.getProgramaId())
                .nombre(a.getNombre())
                .correoMatricula(a.getCorreoMatricula())
                .activo(a.getActivo())
                .build();
    }
}
