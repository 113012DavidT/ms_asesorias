
package com.uteq.profesores.snapshot.repository;
import com.uteq.profesores.snapshot.entity.MsDivisionProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MsDivisionProfesorRepository extends JpaRepository<MsDivisionProfesor, Long> {
  List<MsDivisionProfesor> findByProfesorId(Long profesorId);
  Optional<MsDivisionProfesor> findFirstByProfesorIdOrderByIdDesc(Long profesorId);
}
