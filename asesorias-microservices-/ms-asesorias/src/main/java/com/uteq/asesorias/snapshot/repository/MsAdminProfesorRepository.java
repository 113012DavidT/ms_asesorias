
package com.uteq.asesorias.snapshot.repository;
import com.uteq.asesorias.snapshot.entity.MsAdminProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MsAdminProfesorRepository extends JpaRepository<MsAdminProfesor, Long> {
  Optional<MsAdminProfesor> findFirstByProfesorUsuarioIdOrderByIdDesc(Long profesorUsuarioId);
}
