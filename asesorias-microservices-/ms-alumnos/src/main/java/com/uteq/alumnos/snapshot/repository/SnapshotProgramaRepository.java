package com.uteq.alumnos.snapshot.repository;

import com.uteq.alumnos.snapshot.entity.SnapshotPrograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotProgramaRepository extends JpaRepository<SnapshotPrograma, Long> {
}
