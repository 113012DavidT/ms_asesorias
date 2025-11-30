package com.uteq.coordinadores.service;

import java.util.List;
import java.util.Map;

public interface CoordinadorService {
    Map<String, Object> asignarProfesor(Long usuarioId, Map<String, Object> body);
    Map<String, Object> asignarAlumno(Long usuarioId, Map<String, Object> body);
    List<Map<String, Object>> listarProfesores();
    List<Map<String, Object>> listarAlumnos();
    Map<String, Object> listarDivisionesYProgramas();
}
