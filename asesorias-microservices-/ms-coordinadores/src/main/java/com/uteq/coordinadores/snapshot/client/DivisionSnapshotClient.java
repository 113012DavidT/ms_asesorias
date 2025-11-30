package com.uteq.coordinadores.snapshot.client;

import com.uteq.coordinadores.snapshot.dto.DivisionDTO;
import com.uteq.coordinadores.snapshot.dto.ProgramaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-divisiones", contextId = "divisionSnapshotClient")
public interface DivisionSnapshotClient {

    @GetMapping("/api/divisiones/{id}")
    DivisionDTO obtenerDivision(@PathVariable Long id);

    @GetMapping("/api/programas/{id}")
    ProgramaDTO obtenerPrograma(@PathVariable Long id);
}
