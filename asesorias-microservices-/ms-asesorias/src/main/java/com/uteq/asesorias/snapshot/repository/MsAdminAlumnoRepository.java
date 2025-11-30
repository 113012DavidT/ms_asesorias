
package com.uteq.asesorias.snapshot.repository;
import com.uteq.asesorias.snapshot.entity.MsAdminAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MsAdminAlumnoRepository extends JpaRepository<MsAdminAlumno, Long> {
  Optional<MsAdminAlumno> findFirstByAlumnoUsuarioIdOrderByIdDesc(Long alumnoUsuarioId);
}
