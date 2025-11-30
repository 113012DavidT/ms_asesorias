package com.uteq.admin.controller;

import com.uteq.admin.dto.ProgramaDTO;
import com.uteq.admin.entity.Programa;
import com.uteq.admin.service.ProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/programas")
@RequiredArgsConstructor
public class ProgramaController {
    private final ProgramaService service;

    @PostMapping
    public ResponseEntity<Programa> crear(@RequestBody ProgramaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Programa> actualizar(@PathVariable Long id, @RequestBody ProgramaDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Programa> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Programa>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/activos/listar")
    public ResponseEntity<List<Programa>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
