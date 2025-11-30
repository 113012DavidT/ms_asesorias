package com.uteq.asesorias.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
@FeignClient(name="ms-admin", contextId = "asesoriasAdminClient")
public interface AdminClient {
  @GetMapping("/api/admin/usuarios/{id}")
  Map<String,Object> obtenerUsuario(@PathVariable("id") Long id);
  @GetMapping("/api/admin/perfiles/profesor/{usuarioId}")
  Map<String,Object> perfilProfesor(@PathVariable("usuarioId") Long usuarioId);
  @GetMapping("/api/admin/perfiles/alumno/{usuarioId}")
  Map<String,Object> perfilAlumno(@PathVariable("usuarioId") Long usuarioId);
}
