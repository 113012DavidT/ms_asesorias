package com.uteq.admin.client;

import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.dto.ProgramaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(
    name = "ms-divisiones-client",
    url = "http://localhost:8082",
    contextId = "msDivisionesClient"
)
public interface MsDivisionesClient {

    @GetMapping("/api/divisiones")
    List<DivisionDTO> obtenerDivisiones();

    @GetMapping("/api/divisiones/{id}")
    DivisionDTO obtenerDivision(@PathVariable Long id);

    @GetMapping("/api/programas")
    List<ProgramaDTO> obtenerProgramas();

    @GetMapping("/api/programas/division/{divisionId}")
    List<ProgramaDTO> obtenerProgramasPorDivision(@PathVariable Long divisionId);
}
