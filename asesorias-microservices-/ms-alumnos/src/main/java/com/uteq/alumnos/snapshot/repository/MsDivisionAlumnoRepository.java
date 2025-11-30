
package com.uteq.alumnos.snapshot.repository;
import com.uteq.alumnos.snapshot.entity.MsDivisionAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MsDivisionAlumnoRepository extends JpaRepository<MsDivisionAlumno, Long> {
  List<MsDivisionAlumno> findByAlumnoId(Long alumnoId);
  Optional<MsDivisionAlumno> findFirstByAlumnoIdOrderByIdDesc(Long alumnoId);
}
