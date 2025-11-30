package com.uteq.asesorias.snapshot.repository;

import com.uteq.asesorias.snapshot.entity.SnapshotProfesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotProfesorRepository extends JpaRepository<SnapshotProfesor, Long> {
}
