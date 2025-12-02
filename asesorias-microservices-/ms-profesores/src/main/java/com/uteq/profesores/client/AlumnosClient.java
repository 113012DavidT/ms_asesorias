package com.uteq.profesores.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-alumnos", contextId = "profesoresAlumnosClient")
public interface AlumnosClient {
    @GetMapping("/api/alumnos")
    List<Map<String, Object>> listar();
}
