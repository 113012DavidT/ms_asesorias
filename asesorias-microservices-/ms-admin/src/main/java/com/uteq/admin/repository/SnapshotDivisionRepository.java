package com.uteq.admin.repository;

import com.uteq.admin.entity.SnapshotDivision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapshotDivisionRepository extends JpaRepository<SnapshotDivision, Long> {
    SnapshotDivision findByDivisionesId(Long divisionesId);
    SnapshotDivision findByClave(String clave);
}
