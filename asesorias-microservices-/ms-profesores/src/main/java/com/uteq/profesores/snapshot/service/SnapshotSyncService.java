
package com.uteq.profesores.snapshot.service;
import com.uteq.profesores.snapshot.client.DivisionClient;
import com.uteq.profesores.snapshot.dto.DivisionDTO;
import com.uteq.profesores.snapshot.dto.ProgramaDTO;
import com.uteq.profesores.snapshot.entity.MsDivisionProfesor;
import com.uteq.profesores.snapshot.entity.MsProgramaProfesor;
import com.uteq.profesores.snapshot.repository.MsDivisionProfesorRepository;
import com.uteq.profesores.snapshot.repository.MsProgramaProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class SnapshotSyncService {
  private final DivisionClient divisionClient;
  private final MsDivisionProfesorRepository divisionRepo;
  private final MsProgramaProfesorRepository programaRepo;
  @Transactional public MsDivisionProfesor syncDivision(Long profesorId, Long divisionId){
    DivisionDTO d = divisionClient.obtenerDivision(divisionId);
    return divisionRepo.save(MsDivisionProfesor.builder()
      .profesorId(profesorId).divisionId(divisionId)
      .divisionClave(d.getClave()).divisionNombre(d.getNombre()).divisionDescripcion(d.getDescripcion())
      .version(1L).build());
  }
  @Transactional public MsProgramaProfesor syncPrograma(Long profesorId, Long programaId){
    ProgramaDTO p = divisionClient.obtenerPrograma(programaId);
    return programaRepo.save(MsProgramaProfesor.builder()
      .profesorId(profesorId).programaId(programaId)
      .programaClave(p.getClave()).programaNombre(p.getNombre()).programaDescripcion(p.getDescripcion())
      .version(1L).build());
  }
  public List<MsDivisionProfesor> divisionesByProfesor(Long profesorId){ return divisionRepo.findByProfesorId(profesorId); }
  public List<MsProgramaProfesor> programasByProfesor(Long profesorId){ return programaRepo.findByProfesorId(profesorId); }
}
