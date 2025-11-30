package com.uteq.alumnos.service;

import com.uteq.alumnos.dto.AlumnoDTO;
import com.uteq.alumnos.entity.Alumno;

import java.util.List;

public interface AlumnoService {
    Alumno crear(Alumno a);
    List<AlumnoDTO> listar();
    Alumno obtener(Long id);
    Alumno obtenerPorUsuario(Long usuarioId);
    Alumno actualizar(Long id, Alumno a);
    void eliminar(Long id);
    List<Alumno> listarPorDivisionYPrograma(Long divisionId, Long programaId);
}
