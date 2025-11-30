package com.uteq.profesores.repository;

import com.uteq.profesores.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Profesor findByUsuarioId(Long usuarioId);
    List<Profesor> findByDivisionIdAndProgramaId(Long divisionId, Long programaId);
}
