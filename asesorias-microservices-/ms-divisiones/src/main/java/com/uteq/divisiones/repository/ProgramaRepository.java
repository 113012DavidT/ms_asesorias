package com.uteq.divisiones.repository;

import com.uteq.divisiones.entity.ProgramaEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgramaRepository extends JpaRepository<ProgramaEducativo, Long> {
    List<ProgramaEducativo> findByDivisionId(Long divisionId);
}
