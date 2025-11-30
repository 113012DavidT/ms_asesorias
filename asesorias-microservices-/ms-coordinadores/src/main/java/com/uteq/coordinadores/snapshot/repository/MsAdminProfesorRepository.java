package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.MsAdminProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsAdminProfesorRepository extends JpaRepository<MsAdminProfesor, Long> {
    MsAdminProfesor findByProfesorId(Long profesorId);
}
