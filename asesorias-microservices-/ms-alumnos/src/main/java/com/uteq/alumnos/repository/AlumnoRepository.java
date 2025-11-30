package com.uteq.alumnos.repository;

import com.uteq.alumnos.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Alumno findByUsuarioId(Long usuarioId);
    List<Alumno> findByDivisionIdAndProgramaId(Long divisionId, Long programaId);
}
