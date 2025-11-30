package com.uteq.coordinadores.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@FeignClient(name = "ms-divisiones", contextId = "divisionClient")
public interface DivisionClient {

    @GetMapping("/api/divisiones")
    List<Map<String, Object>> listarDivisiones();

    @GetMapping("/api/programas")
    List<Map<String, Object>> listarProgramas();
}
