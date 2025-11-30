package com.uteq.admin.controller;

import com.uteq.admin.dto.GrupoDTO;
import com.uteq.admin.entity.Grupo;
import com.uteq.admin.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {
    private final GrupoService service;

    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody GrupoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> actualizar(@PathVariable Long id, @RequestBody GrupoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Grupo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/activos/listar")
    public ResponseEntity<List<Grupo>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Grupo>> listarPorProfesor(@PathVariable Long profesorId) {
        return ResponseEntity.ok(service.listarPorProfesor(profesorId));
    }

    @GetMapping("/division/{divisionId}")
    public ResponseEntity<List<Grupo>> listarPorDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(service.listarPorDivision(divisionId));
    }

    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<Grupo>> listarPorPrograma(@PathVariable Long programaId) {
        return ResponseEntity.ok(service.listarPorPrograma(programaId));
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
