package com.uteq.admin.controller;

import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.entity.Division;
import com.uteq.admin.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/divisiones")
@RequiredArgsConstructor
public class DivisionController {
    private final DivisionService service;

    @PostMapping
    public ResponseEntity<Division> crear(@RequestBody DivisionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Division> actualizar(@PathVariable Long id, @RequestBody DivisionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Division> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Division>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/activos/listar")
    public ResponseEntity<List<Division>> listarActivas() {
        return ResponseEntity.ok(service.listarActivas());
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
