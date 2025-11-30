package com.uteq.profesores.service;

import com.uteq.profesores.dto.ProfesorDTO;
import com.uteq.profesores.entity.Profesor;

import java.util.List;

public interface ProfesorService {
    Profesor crear(Profesor p);
    List<ProfesorDTO> listar();
    Profesor obtener(Long id);
    Profesor obtenerPorUsuario(Long usuarioId);
    Profesor actualizar(Long id, Profesor p);
    void eliminar(Long id);
    List<Profesor> listarPorDivisionYPrograma(Long divisionId, Long programaId);
}
