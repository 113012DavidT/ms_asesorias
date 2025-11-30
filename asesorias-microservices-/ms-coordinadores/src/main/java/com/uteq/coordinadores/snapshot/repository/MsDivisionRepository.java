package com.uteq.coordinadores.snapshot.repository;

import com.uteq.coordinadores.snapshot.entity.MsDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsDivisionRepository extends JpaRepository<MsDivision, Long> {
    MsDivision findByDivisionId(Long divisionId);
    List<MsDivision> findAll();
}
