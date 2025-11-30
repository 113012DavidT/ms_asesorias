
package com.uteq.asesorias.snapshot.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@FeignClient(name = "ms-admin", contextId = "asesoriasAdminSnapshotClient")
public interface AdminClient {
  @GetMapping("/api/admin/perfiles/profesor/{usuarioId}") Map<String,Object> perfilProfesor(@PathVariable Long usuarioId);
  @GetMapping("/api/admin/perfiles/alumno/{usuarioId}")   Map<String,Object> perfilAlumno(@PathVariable Long usuarioId);
}
