package com.uteq.profesores.repository;

import com.uteq.profesores.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByProfesorUsuarioIdAndActivoTrueOrderByCreadoEnDesc(Long profesorUsuarioId);
}
