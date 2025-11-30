
package com.uteq.profesores.snapshot.controller;
import com.uteq.profesores.snapshot.entity.MsDivisionProfesor;
import com.uteq.profesores.snapshot.entity.MsProgramaProfesor;
import com.uteq.profesores.snapshot.service.SnapshotSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/profesores") @RequiredArgsConstructor
public class SnapshotController {
  private final SnapshotSyncService service;
  @PostMapping("/internal/sync/division/{divisionId}")
  public MsDivisionProfesor syncDivision(@RequestParam Long profesorId, @PathVariable Long divisionId){
    return service.syncDivision(profesorId, divisionId);
  }
  @PostMapping("/internal/sync/programa/{programaId}")
  public MsProgramaProfesor syncPrograma(@RequestParam Long profesorId, @PathVariable Long programaId){
    return service.syncPrograma(profesorId, programaId);
  }
  @GetMapping("/snapshots/divisiones")
  public List<MsDivisionProfesor> divisiones(@RequestParam Long profesorId){
    return service.divisionesByProfesor(profesorId);
  }
  @GetMapping("/snapshots/programas")
  public List<MsProgramaProfesor> programas(@RequestParam Long profesorId){
    return service.programasByProfesor(profesorId);
  }
}
