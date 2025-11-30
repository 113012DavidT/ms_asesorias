package com.uteq.admin.repository;

import com.uteq.admin.entity.SnapshotPrograma;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SnapshotProgramaRepository extends JpaRepository<SnapshotPrograma, Long> {
    SnapshotPrograma findByProgramasId(Long programasId);
    SnapshotPrograma findByClave(String clave);
    List<SnapshotPrograma> findByDivisionId(Long divisionId);
}
