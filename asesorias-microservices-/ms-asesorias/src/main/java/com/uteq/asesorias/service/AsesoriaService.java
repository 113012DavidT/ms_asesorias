package com.uteq.asesorias.service;

import com.uteq.asesorias.entity.Asesoria;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AsesoriaService {
    // Creación
    Asesoria crear(Long profesorId, Long alumnoId, Long disponibilidadId, String materia, String observaciones);
    Asesoria crearPorProfesor(Long profesorId, Long alumnoId, LocalDate fecha,
                              LocalTime horaInicio, LocalTime horaFin,
                              String titulo, String materia, String observaciones);
    
    // Lectura
    List<Asesoria> porProfesor(Long profesorId);
    List<Asesoria> porAlumno(Long alumnoId);
    Optional<Asesoria> obtenerPorId(Long id);
    List<Asesoria> listarTodas();
    
    // Actualización
    Asesoria actualizar(Long id, Asesoria asesoria);
    Asesoria cambiarEstatus(Long id, String nuevoEstatus);
    Asesoria asignarDisponibilidad(Long id, Long disponibilidadId);
    
    // Eliminación
    void eliminar(Long id);
    
    // Búsquedas especiales
    List<Asesoria> porFecha(LocalDate fecha);
    List<Asesoria> porEstatus(String estatus);
    List<Asesoria> porProfesorYFecha(Long profesorId, LocalDate fecha);
}

