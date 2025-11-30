package com.uteq.admin.controller;

import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.dto.ProgramaDTO;
import com.uteq.admin.service.SnapshotSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/coordinador")
@RequiredArgsConstructor
public class CoordinadorController {

    private final SnapshotSyncService syncService;

    /**
     * Sincronizar divisiones desde ms-divisiones
     */
    @PostMapping("/sincronizar-divisiones")
    public void sincronizarDivisiones() {
        syncService.sincronizarDivisiones();
    }

    /**
     * Sincronizar programas desde ms-divisiones
     */
    @PostMapping("/sincronizar-programas")
    public void sincronizarProgramas() {
        syncService.sincronizarProgramas();
    }

    /**
     * Obtener todas las divisiones disponibles (del snapshot)
     */
    @GetMapping("/divisiones")
    public List<DivisionDTO> obtenerDivisiones() {
        return syncService.obtenerDivisiones();
    }

    /**
     * Obtener todos los programas disponibles (del snapshot)
     */
    @GetMapping("/programas")
    public List<ProgramaDTO> obtenerProgramas() {
        return syncService.obtenerProgramas();
    }

    /**
     * Obtener programas de una división específica
     */
    @GetMapping("/programas/division/{divisionId}")
    public List<ProgramaDTO> obtenerProgramasPorDivision(@PathVariable Long divisionId) {
        return syncService.obtenerProgramasPorDivision(divisionId);
    }
}
