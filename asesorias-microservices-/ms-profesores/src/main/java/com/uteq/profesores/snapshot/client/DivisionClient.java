
package com.uteq.profesores.snapshot.client;
import com.uteq.profesores.snapshot.dto.DivisionDTO;
import com.uteq.profesores.snapshot.dto.ProgramaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "ms-divisiones", contextId = "profesoresDivisionSnapshotClient")
public interface DivisionClient {
  @GetMapping("/api/divisiones/{id}") DivisionDTO obtenerDivision(@PathVariable Long id);
  @GetMapping("/api/programas/{id}")  ProgramaDTO  obtenerPrograma(@PathVariable Long id);
}
