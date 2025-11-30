package com.uteq.admin.repository;
import com.uteq.admin.entity.AlumnoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AlumnoPerfilRepository extends JpaRepository<AlumnoPerfil, Long> {
  Optional<AlumnoPerfil> findByUsuarioId(Long usuarioId);
}
