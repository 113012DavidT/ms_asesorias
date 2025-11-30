package com.uteq.admin.service;

import com.uteq.admin.dto.GrupoDTO;
import com.uteq.admin.entity.Grupo;
import java.util.List;

public interface GrupoService {
    Grupo crear(GrupoDTO dto);
    Grupo actualizar(Long id, GrupoDTO dto);
    Grupo obtenerPorId(Long id);
    List<Grupo> listarTodos();
    List<Grupo> listarActivos();
    List<Grupo> listarPorProfesor(Long profesorId);
    List<Grupo> listarPorDivision(Long divisionId);
    List<Grupo> listarPorPrograma(Long programaId);
    void eliminar(Long id);
    void desactivar(Long id);
}
