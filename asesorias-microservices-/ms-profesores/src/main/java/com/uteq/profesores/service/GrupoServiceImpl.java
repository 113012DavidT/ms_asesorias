package com.uteq.profesores.service;

import com.uteq.profesores.entity.Grupo;
import com.uteq.profesores.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository repo;

    @Override
    public List<Grupo> listarPorProfesor(Long profesorUsuarioId) {
        return repo.findByProfesorUsuarioIdAndActivoTrueOrderByCreadoEnDesc(profesorUsuarioId);
    }

    @Override
    @Transactional
    public Grupo crear(Long profesorUsuarioId, String nombre, String descripcion) {
        Grupo g = Grupo.builder()
                .profesorUsuarioId(profesorUsuarioId)
                .nombre(nombre)
                .descripcion(descripcion)
                .build();
        if (g.getCreadoEn() == null) {
            g.setCreadoEn(java.time.LocalDateTime.now());
        }
        if (g.getActivo() == null) {
            g.setActivo(true);
        }
        return repo.save(g);
    }

    @Override
    @Transactional
    public Grupo actualizar(Long profesorUsuarioId, Long id, String nombre, String descripcion) {
        Grupo g = repo.findById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        if (!g.getProfesorUsuarioId().equals(profesorUsuarioId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "No puede modificar grupos de otros profesores");
        }
        if (nombre != null && !nombre.isBlank()) g.setNombre(nombre.trim());
        if (descripcion != null) g.setDescripcion(descripcion);
        return repo.save(g);
    }

    @Override
    @Transactional
    public void eliminar(Long profesorUsuarioId, Long id) {
        Grupo g = repo.findById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        if (!g.getProfesorUsuarioId().equals(profesorUsuarioId)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "No puede eliminar grupos de otros profesores");
        }
        g.setActivo(false);
        repo.save(g);
    }
}
