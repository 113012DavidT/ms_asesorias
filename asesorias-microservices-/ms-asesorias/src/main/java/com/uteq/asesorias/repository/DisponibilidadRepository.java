package com.uteq.asesorias.repository;

import com.uteq.asesorias.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    // Búsquedas por profesor
    List<Disponibilidad> findByProfesorId(Long profesorId);
    List<Disponibilidad> findByProfesorIdAndFecha(Long profesorId, LocalDate fecha);
    
    // Búsquedas por disponibilidad
    List<Disponibilidad> findByDisponibleTrue();
    List<Disponibilidad> findByDisponibleFalse();
    
    // Búsquedas complejas
    Optional<Disponibilidad> findByProfesorIdAndFechaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualAndDisponibleTrue(
            Long profesorId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin
    );
}

