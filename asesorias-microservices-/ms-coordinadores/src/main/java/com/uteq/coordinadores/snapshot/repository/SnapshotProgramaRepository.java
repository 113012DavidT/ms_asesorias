package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.SnapshotPrograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotProgramaRepository extends JpaRepository<SnapshotPrograma, Long> {
}
