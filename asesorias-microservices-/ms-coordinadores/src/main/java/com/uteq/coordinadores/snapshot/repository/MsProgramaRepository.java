package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.MsPrograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsProgramaRepository extends JpaRepository<MsPrograma, Long> {
    MsPrograma findByProgramaId(Long programaId);
    List<MsPrograma> findByDivisionId(Long divisionId);
    List<MsPrograma> findAll();
}
