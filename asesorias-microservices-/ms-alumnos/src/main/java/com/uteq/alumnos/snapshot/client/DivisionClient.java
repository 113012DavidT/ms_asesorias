
package com.uteq.alumnos.snapshot.client;
import com.uteq.alumnos.snapshot.dto.DivisionDTO;
import com.uteq.alumnos.snapshot.dto.ProgramaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "ms-divisiones", contextId = "alumnosDivisionSnapshotClient")
public interface DivisionClient {
  @GetMapping("/api/divisiones/{id}") DivisionDTO obtenerDivision(@PathVariable Long id);
  @GetMapping("/api/programas/{id}")  ProgramaDTO  obtenerPrograma(@PathVariable Long id);
}
