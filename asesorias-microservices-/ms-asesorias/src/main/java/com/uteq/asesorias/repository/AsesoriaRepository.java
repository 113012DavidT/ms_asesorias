package com.uteq.asesorias.repository;

import com.uteq.asesorias.entity.Asesoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AsesoriaRepository extends JpaRepository<Asesoria, Long> {
    List<Asesoria> findByProfesorId(Long profesorId);
    List<Asesoria> findByAlumnoId(Long alumnoId);
    List<Asesoria> findByFecha(LocalDate fecha);
    List<Asesoria> findByEstatus(String estatus);
    List<Asesoria> findByProfesorIdAndFecha(Long profesorId, LocalDate fecha);
    List<Asesoria> findByAlumnoIdAndEstatus(Long alumnoId, String estatus);
    List<Asesoria> findByProfesorIdAndEstatus(Long profesorId, String estatus);
}

