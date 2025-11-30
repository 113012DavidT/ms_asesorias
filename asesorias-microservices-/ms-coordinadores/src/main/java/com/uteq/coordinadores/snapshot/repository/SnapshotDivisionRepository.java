package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.SnapshotDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotDivisionRepository extends JpaRepository<SnapshotDivision, Long> {
}
