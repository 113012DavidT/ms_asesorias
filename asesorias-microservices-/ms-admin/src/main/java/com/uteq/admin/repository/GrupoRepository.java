package com.uteq.admin.repository;

import com.uteq.admin.entity.Grupo;
import com.uteq.admin.entity.Division;
import com.uteq.admin.entity.Programa;
import com.uteq.admin.entity.ProfesorPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByProfesor(ProfesorPerfil profesor);
    List<Grupo> findByDivision(Division division);
    List<Grupo> findByPrograma(Programa programa);
    List<Grupo> findByProfesorAndDivision(ProfesorPerfil profesor, Division division);
    List<Grupo> findByActivo(Boolean activo);
    Optional<Grupo> findByNombreAndProfesor(String nombre, ProfesorPerfil profesor);
}
