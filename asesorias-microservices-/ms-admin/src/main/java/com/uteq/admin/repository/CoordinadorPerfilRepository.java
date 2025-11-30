package com.uteq.admin.repository;

import com.uteq.admin.entity.CoordinadorPerfil;
import com.uteq.admin.entity.Division;
import com.uteq.admin.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinadorPerfilRepository extends JpaRepository<CoordinadorPerfil, Long> {
    Optional<CoordinadorPerfil> findByUsuarioId(Long usuarioId);
    List<CoordinadorPerfil> findByDivision(Division division);
    List<CoordinadorPerfil> findByPrograma(Programa programa);
    List<CoordinadorPerfil> findByActivo(Boolean activo);
}
