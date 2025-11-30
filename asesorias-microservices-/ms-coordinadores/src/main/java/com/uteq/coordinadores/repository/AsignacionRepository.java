package com.uteq.coordinadores.repository;

import com.uteq.coordinadores.entity.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByRol(String rol);
    Asignacion findByUsuarioIdAndRol(Long usuarioId, String rol);
}
