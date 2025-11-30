package com.uteq.alumnos.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@FeignClient(name="ms-asesorias", contextId = "alumnosAsesoriasClient")
public interface AsesoriasClient {
  @GetMapping("/api/asesorias/alumno/{alumnoId}")
  List<Map<String,Object>> asesoriasPorAlumno(@PathVariable("alumnoId") Long alumnoId);
  @PostMapping("/api/asesorias")
  Map<String,Object> crearAsesoria(@RequestBody Map<String,Object> body);
}
