package com.uteq.alumnos.snapshot.repository;

import com.uteq.alumnos.snapshot.entity.SnapshotUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotUsuarioRepository extends JpaRepository<SnapshotUsuario, Long> {
}
