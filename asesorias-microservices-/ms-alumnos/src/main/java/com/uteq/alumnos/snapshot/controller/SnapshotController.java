
package com.uteq.alumnos.snapshot.controller;
import com.uteq.alumnos.snapshot.entity.MsDivisionAlumno;
import com.uteq.alumnos.snapshot.entity.MsProgramaAlumno;
import com.uteq.alumnos.snapshot.service.SnapshotSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/alumnos") @RequiredArgsConstructor
public class SnapshotController {
  private final SnapshotSyncService service;
  @PostMapping("/internal/sync/division/{divisionId}")
  public MsDivisionAlumno syncDivision(@RequestParam Long alumnoId, @PathVariable Long divisionId){
    return service.syncDivision(alumnoId, divisionId);
  }
  @PostMapping("/internal/sync/programa/{programaId}")
  public MsProgramaAlumno syncPrograma(@RequestParam Long alumnoId, @PathVariable Long programaId){
    return service.syncPrograma(alumnoId, programaId);
  }
  @GetMapping("/snapshots/divisiones")
  public List<MsDivisionAlumno> divisiones(@RequestParam Long alumnoId){
    return service.divisionesByAlumno(alumnoId);
  }
  @GetMapping("/snapshots/programas")
  public List<MsProgramaAlumno> programas(@RequestParam Long alumnoId){
    return service.programasByAlumno(alumnoId);
  }
}
