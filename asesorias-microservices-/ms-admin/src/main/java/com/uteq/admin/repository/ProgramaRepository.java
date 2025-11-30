package com.uteq.admin.repository;

import com.uteq.admin.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    Optional<Programa> findByNombre(String nombre);
    List<Programa> findByActivo(Boolean activo);
}
