package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.MsAdminAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsAdminAlumnoRepository extends JpaRepository<MsAdminAlumno, Long> {
    MsAdminAlumno findByAlumnoId(Long alumnoId);
}
