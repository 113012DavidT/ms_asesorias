package com.uteq.profesores.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name="ms-admin", contextId = "profesoresAdminClient")
public interface AdminClient {
  @GetMapping("/api/admin/usuarios/{id}")
  Map<String,Object> usuario(@PathVariable("id") Long id);

  @GetMapping("/api/admin/perfiles/profesor/{usuarioId}")
  Map<String,Object> perfilProfesor(@PathVariable("usuarioId") Long usuarioId);

  @GetMapping("/api/admin/roles")
  List<Map<String,Object>> roles();

  @GetMapping("/api/admin/usuarios/rol/{rolId}")
  List<Map<String,Object>> usuariosPorRol(@PathVariable("rolId") Long rolId);
}
