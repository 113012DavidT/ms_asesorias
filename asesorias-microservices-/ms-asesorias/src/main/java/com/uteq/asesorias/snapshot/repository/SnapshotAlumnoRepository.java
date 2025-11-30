package com.uteq.asesorias.snapshot.repository;

import com.uteq.asesorias.snapshot.entity.SnapshotAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotAlumnoRepository extends JpaRepository<SnapshotAlumno, Long> {
}
