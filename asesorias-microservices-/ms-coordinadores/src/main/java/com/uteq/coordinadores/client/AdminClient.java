package com.uteq.coordinadores.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-admin", contextId = "adminClient")
public interface AdminClient {

    @GetMapping("/api/admin/perfiles/profesor")
    List<Map<String, Object>> listarProfesores();

    @GetMapping("/api/admin/perfiles/alumno")
    List<Map<String, Object>> listarAlumnos();

    @PostMapping("/api/admin/perfiles/profesor")
    Map<String, Object> actualizarPerfilProfesor(@RequestBody Map<String, Object> body);

    @PostMapping("/api/admin/perfiles/alumno")
    Map<String, Object> actualizarPerfilAlumno(@RequestBody Map<String, Object> body);
}
