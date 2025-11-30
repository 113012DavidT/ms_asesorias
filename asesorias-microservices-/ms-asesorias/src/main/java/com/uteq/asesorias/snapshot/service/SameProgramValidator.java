
package com.uteq.asesorias.snapshot.service;
import com.uteq.asesorias.snapshot.client.AdminClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
@Component @RequiredArgsConstructor
public class SameProgramValidator {
  private final AdminClient adminClient;
  private final com.uteq.asesorias.snapshot.repository.MsAdminProfesorRepository profRepo;
  private final com.uteq.asesorias.snapshot.repository.MsAdminAlumnoRepository alumRepo;
  public boolean validate(Long profesorUsuarioId, Long alumnoUsuarioId){
    try {
      Map<String,Object> p = adminClient.perfilProfesor(profesorUsuarioId);
      Map<String,Object> a = adminClient.perfilAlumno(alumnoUsuarioId);
      Long pp = p.get("programaId")==null ? null : Long.valueOf(p.get("programaId").toString());
      Long ap = a.get("programaId")==null ? null : Long.valueOf(a.get("programaId").toString());
      return pp!=null && pp.equals(ap);
    } catch(Exception ex){
      var p = profRepo.findFirstByProfesorUsuarioIdOrderByIdDesc(profesorUsuarioId).orElse(null);
      var a = alumRepo.findFirstByAlumnoUsuarioIdOrderByIdDesc(alumnoUsuarioId).orElse(null);
      return p!=null && a!=null && p.getProgramaId().equals(a.getProgramaId());
    }
  }
}
