package com.uteq.profesores.service;

import com.uteq.profesores.entity.Grupo;

import java.util.List;

public interface GrupoService {
    List<Grupo> listarPorProfesor(Long profesorUsuarioId);
    Grupo crear(Long profesorUsuarioId, String nombre, String descripcion);
    Grupo actualizar(Long profesorUsuarioId, Long id, String nombre, String descripcion);
    void eliminar(Long profesorUsuarioId, Long id);
}
