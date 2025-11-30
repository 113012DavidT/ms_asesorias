
package com.uteq.asesorias.snapshot.controller;
import com.uteq.asesorias.snapshot.dto.PerfilAsignacionDTO;
import com.uteq.asesorias.snapshot.entity.MsAdminAlumno;
import com.uteq.asesorias.snapshot.entity.MsAdminProfesor;
import com.uteq.asesorias.snapshot.service.SnapshotPerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/asesorias/internal") @RequiredArgsConstructor
public class InternalSyncController {
  private final SnapshotPerfilService service;
  @PostMapping("/sync/perfil-profesor/{usuarioId}")
  public MsAdminProfesor syncPerfilProfesor(@PathVariable Long usuarioId, @RequestBody PerfilAsignacionDTO dto){
    return service.saveProfesor(usuarioId, dto);
  }
  @PostMapping("/sync/perfil-alumno/{usuarioId}")
  public MsAdminAlumno syncPerfilAlumno(@PathVariable Long usuarioId, @RequestBody PerfilAsignacionDTO dto){
    return service.saveAlumno(usuarioId, dto);
  }
}
