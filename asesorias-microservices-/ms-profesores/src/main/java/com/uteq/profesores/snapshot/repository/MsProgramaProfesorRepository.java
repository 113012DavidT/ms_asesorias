
package com.uteq.profesores.snapshot.repository;
import com.uteq.profesores.snapshot.entity.MsProgramaProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MsProgramaProfesorRepository extends JpaRepository<MsProgramaProfesor, Long> {
  List<MsProgramaProfesor> findByProfesorId(Long profesorId);
  Optional<MsProgramaProfesor> findFirstByProfesorIdOrderByIdDesc(Long profesorId);
}
