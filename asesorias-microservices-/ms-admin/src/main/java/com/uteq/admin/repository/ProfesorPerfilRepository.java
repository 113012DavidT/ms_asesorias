package com.uteq.admin.repository;
import com.uteq.admin.entity.ProfesorPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ProfesorPerfilRepository extends JpaRepository<ProfesorPerfil, Long> {
  Optional<ProfesorPerfil> findByUsuarioId(Long usuarioId);
}
