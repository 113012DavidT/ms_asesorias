
package com.uteq.alumnos.snapshot.repository;
import com.uteq.alumnos.snapshot.entity.MsProgramaAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MsProgramaAlumnoRepository extends JpaRepository<MsProgramaAlumno, Long> {
  List<MsProgramaAlumno> findByAlumnoId(Long alumnoId);
  Optional<MsProgramaAlumno> findFirstByAlumnoIdOrderByIdDesc(Long alumnoId);
}
