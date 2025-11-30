package com.uteq.admin.controller;

import com.uteq.admin.dto.CoordinadorPerfilDTO;
import com.uteq.admin.entity.CoordinadorPerfil;
import com.uteq.admin.service.CoordinadorPerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coordinadores")
@RequiredArgsConstructor
public class CoordinadorPerfilController {
    private final CoordinadorPerfilService service;

    @PostMapping
    public ResponseEntity<CoordinadorPerfil> crear(@RequestBody CoordinadorPerfilDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoordinadorPerfil> actualizar(@PathVariable Long id, @RequestBody CoordinadorPerfilDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinadorPerfil> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Optional<CoordinadorPerfil>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerPorUsuarioId(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<CoordinadorPerfil>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/activos/listar")
    public ResponseEntity<List<CoordinadorPerfil>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/division/{divisionId}")
    public ResponseEntity<List<CoordinadorPerfil>> listarPorDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(service.listarPorDivision(divisionId));
    }

    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<CoordinadorPerfil>> listarPorPrograma(@PathVariable Long programaId) {
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
