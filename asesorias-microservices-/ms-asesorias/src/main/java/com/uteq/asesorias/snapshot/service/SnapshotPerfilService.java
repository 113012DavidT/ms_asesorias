
package com.uteq.asesorias.snapshot.service;
import com.uteq.asesorias.snapshot.dto.PerfilAsignacionDTO;
import com.uteq.asesorias.snapshot.entity.MsAdminAlumno;
import com.uteq.asesorias.snapshot.entity.MsAdminProfesor;
import com.uteq.asesorias.snapshot.repository.MsAdminAlumnoRepository;
import com.uteq.asesorias.snapshot.repository.MsAdminProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class SnapshotPerfilService {
  private final MsAdminProfesorRepository profRepo;
  private final MsAdminAlumnoRepository alumRepo;
  @Transactional public MsAdminProfesor saveProfesor(Long profesorUsuarioId, PerfilAsignacionDTO dto){
    return profRepo.save(MsAdminProfesor.builder()
      .profesorUsuarioId(profesorUsuarioId).divisionId(dto.getDivisionId()).programaId(dto.getProgramaId()).version(1L).build());
  }
  @Transactional public MsAdminAlumno saveAlumno(Long alumnoUsuarioId, PerfilAsignacionDTO dto){
    return alumRepo.save(MsAdminAlumno.builder()
      .alumnoUsuarioId(alumnoUsuarioId).divisionId(dto.getDivisionId()).programaId(dto.getProgramaId()).version(1L).build());
  }
}
