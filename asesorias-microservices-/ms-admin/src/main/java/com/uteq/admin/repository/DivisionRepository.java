package com.uteq.admin.repository;

import com.uteq.admin.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    Optional<Division> findByNombre(String nombre);
    List<Division> findByActivo(Boolean activo);
}
