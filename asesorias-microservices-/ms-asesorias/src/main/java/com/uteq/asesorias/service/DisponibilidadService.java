package com.uteq.asesorias.service;

import com.uteq.asesorias.entity.Disponibilidad;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadService {
    // Creación
    Disponibilidad crear(Disponibilidad d);
    
    // Lectura
    List<Disponibilidad> porProfesor(Long profesorId);
    List<Disponibilidad> porProfesorYFecha(Long profesorId, LocalDate fecha);
    List<Disponibilidad> disponibles();
    List<Disponibilidad> nodisponibles();
    Optional<Disponibilidad> obtenerPorId(Long id);
    List<Disponibilidad> listarTodas();
    
    // Actualización
    Disponibilidad actualizar(Long id, Disponibilidad disponibilidad);
    Disponibilidad marcarDisponible(Long id, Boolean disponible);
    
    // Eliminación
    void eliminar(Long id);
}

