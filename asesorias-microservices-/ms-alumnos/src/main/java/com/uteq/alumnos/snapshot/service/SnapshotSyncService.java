
package com.uteq.alumnos.snapshot.service;
import com.uteq.alumnos.snapshot.client.DivisionClient;
import com.uteq.alumnos.snapshot.dto.DivisionDTO;
import com.uteq.alumnos.snapshot.dto.ProgramaDTO;
import com.uteq.alumnos.snapshot.entity.MsDivisionAlumno;
import com.uteq.alumnos.snapshot.entity.MsProgramaAlumno;
import com.uteq.alumnos.snapshot.repository.MsDivisionAlumnoRepository;
import com.uteq.alumnos.snapshot.repository.MsProgramaAlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class SnapshotSyncService {
  private final DivisionClient divisionClient;
  private final MsDivisionAlumnoRepository divisionRepo;
  private final MsProgramaAlumnoRepository programaRepo;
  @Transactional public MsDivisionAlumno syncDivision(Long alumnoId, Long divisionId){
    DivisionDTO d = divisionClient.obtenerDivision(divisionId);
    return divisionRepo.save(MsDivisionAlumno.builder()
      .alumnoId(alumnoId).divisionId(divisionId)
      .divisionClave(d.getClave()).divisionNombre(d.getNombre()).divisionDescripcion(d.getDescripcion())
      .version(1L).build());
  }
  @Transactional public MsProgramaAlumno syncPrograma(Long alumnoId, Long programaId){
    ProgramaDTO p = divisionClient.obtenerPrograma(programaId);
    return programaRepo.save(MsProgramaAlumno.builder()
      .alumnoId(alumnoId).programaId(programaId)
      .programaClave(p.getClave()).programaNombre(p.getNombre()).programaDescripcion(p.getDescripcion())
      .version(1L).build());
  }
  public List<MsDivisionAlumno> divisionesByAlumno(Long alumnoId){ return divisionRepo.findByAlumnoId(alumnoId); }
  public List<MsProgramaAlumno> programasByAlumno(Long alumnoId){ return programaRepo.findByAlumnoId(alumnoId); }
}
