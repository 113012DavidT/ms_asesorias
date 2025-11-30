package com.uteq.profesores.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-asesorias", contextId = "profesoresAsesoriasClient")
public interface AsesoriasClient {

    // 🔹 Consultar asesorías por profesor
    @GetMapping("/api/asesorias/profesor/{profesorId}")
    List<Map<String, Object>> asesoriasPorProfesor(@PathVariable Long profesorId);

    // ✅ Crear disponibilidad (ruta correcta en ms-asesorias)
    @PostMapping("/api/disponibilidades")
    Map<String, Object> crearDisponibilidad(@RequestBody Map<String, Object> body);

    // ✅ Crear asesoría proactiva desde el profesor
    @PostMapping("/api/asesorias/profesor")
    Map<String, Object> crearAsesoriaPorProfesor(@RequestBody Map<String, Object> body);
}
