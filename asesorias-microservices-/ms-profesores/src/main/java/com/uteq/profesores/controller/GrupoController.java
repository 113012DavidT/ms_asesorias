package com.uteq.profesores.controller;

import com.uteq.profesores.entity.Grupo;
import com.uteq.profesores.service.GrupoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService grupoService;

    private Long getUserIdOrThrow(jakarta.servlet.http.HttpServletRequest request) {
        String uid = request.getHeader("X-User-Id");
        if (uid == null || uid.isBlank()) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falta X-User-Id");
        try { return Long.valueOf(uid.trim()); } catch (NumberFormatException e) { throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "X-User-Id inválido"); }
    }

    @GetMapping
    public ResponseEntity<List<Grupo>> listar(jakarta.servlet.http.HttpServletRequest request) {
        Long usuarioId = getUserIdOrThrow(request);
        return ResponseEntity.ok(grupoService.listarPorProfesor(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Grupo> crear(jakarta.servlet.http.HttpServletRequest request, @RequestBody CrearReq req) {
        Long usuarioId = getUserIdOrThrow(request);
        if (req == null || req.getNombre() == null || req.getNombre().isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre requerido");
        }
        Grupo g = grupoService.crear(usuarioId, req.getNombre().trim(), req.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(g);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> actualizar(jakarta.servlet.http.HttpServletRequest request, @PathVariable Long id, @RequestBody CrearReq req) {
        Long usuarioId = getUserIdOrThrow(request);
        Grupo g = grupoService.actualizar(usuarioId, id, req.getNombre(), req.getDescripcion());
        return ResponseEntity.ok(g);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(jakarta.servlet.http.HttpServletRequest request, @PathVariable Long id) {
        Long usuarioId = getUserIdOrThrow(request);
        grupoService.eliminar(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CrearReq {
        private String nombre;
        private String descripcion;
    }
}
