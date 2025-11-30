package com.uteq.asesorias.controller;

import com.uteq.asesorias.entity.Disponibilidad;
import com.uteq.asesorias.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService service;

    @PostMapping
    public ResponseEntity<Disponibilidad> crear(@RequestBody Disponibilidad d) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(d));
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidad>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Disponibilidad>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Disponibilidad>> porProfesor(@PathVariable Long profesorId) {
        return ResponseEntity.ok(service.porProfesor(profesorId));
    }

    @GetMapping("/profesor/{profesorId}/fecha/{fecha}")
    public ResponseEntity<List<Disponibilidad>> porProfesorYFecha(
            @PathVariable Long profesorId,
            @PathVariable String fecha) {
        return ResponseEntity.ok(service.porProfesorYFecha(profesorId, LocalDate.parse(fecha)));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Disponibilidad>> disponibles() {
        return ResponseEntity.ok(service.disponibles());
    }

    @GetMapping("/nodisponibles")
    public ResponseEntity<List<Disponibilidad>> nodisponibles() {
        return ResponseEntity.ok(service.nodisponibles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(@PathVariable Long id, @RequestBody Disponibilidad disponibilidad) {
        return ResponseEntity.ok(service.actualizar(id, disponibilidad));
    }

    @PutMapping("/{id}/disponible/{disponible}")
    public ResponseEntity<Disponibilidad> marcarDisponible(
            @PathVariable Long id,
            @PathVariable Boolean disponible) {
        return ResponseEntity.ok(service.marcarDisponible(id, disponible));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

