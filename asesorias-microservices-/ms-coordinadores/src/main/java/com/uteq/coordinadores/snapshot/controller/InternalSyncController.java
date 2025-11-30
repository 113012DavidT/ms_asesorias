package com.uteq.coordinadores.snapshot.controller;

import com.uteq.coordinadores.snapshot.service.SnapshotSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coordinadores/internal/sync")
@RequiredArgsConstructor
public class InternalSyncController {

    private final SnapshotSyncService syncService;

    @PostMapping("/profesor/{profesorId}")
    public Map<String, Object> syncProfesor(@PathVariable Long profesorId) {
        Map<String, Object> res = new HashMap<>();
        res.put("profesor", syncService.syncProfesor(profesorId));
        res.put("timestamp", System.currentTimeMillis());
        return res;
    }

    @PostMapping("/alumno/{alumnoId}")
    public Map<String, Object> syncAlumno(@PathVariable Long alumnoId) {
        Map<String, Object> res = new HashMap<>();
        res.put("alumno", syncService.syncAlumno(alumnoId));
        res.put("timestamp", System.currentTimeMillis());
        return res;
    }

    @PostMapping("/division/{divisionId}")
    public Map<String, Object> syncDivision(@PathVariable Long divisionId) {
        Map<String, Object> res = new HashMap<>();
        res.put("division", syncService.syncDivision(divisionId));
        res.put("timestamp", System.currentTimeMillis());
        return res;
    }

    @PostMapping("/programa/{programaId}")
    public Map<String, Object> syncPrograma(@PathVariable Long programaId) {
        Map<String, Object> res = new HashMap<>();
        res.put("programa", syncService.syncPrograma(programaId));
        res.put("timestamp", System.currentTimeMillis());
        return res;
    }
}
