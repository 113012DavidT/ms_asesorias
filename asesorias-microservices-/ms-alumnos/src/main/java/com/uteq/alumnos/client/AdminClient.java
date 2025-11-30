package com.uteq.alumnos.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
@FeignClient(name="ms-admin", contextId = "alumnosAdminClient")
public interface AdminClient {
  @GetMapping("/api/admin/usuarios/{id}")
  Map<String,Object> usuario(@PathVariable("id") Long id);
  @GetMapping("/api/admin/perfiles/alumno/{usuarioId}")
  Map<String,Object> perfilAlumno(@PathVariable("usuarioId") Long usuarioId);
}
