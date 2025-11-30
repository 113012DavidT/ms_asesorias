package com.uteq.asesorias.controller;

import com.uteq.asesorias.entity.Asesoria;
import com.uteq.asesorias.service.AsesoriaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asesorias")
@RequiredArgsConstructor
public class AsesoriaController {

    private final AsesoriaService service;

    @PostMapping
    public ResponseEntity<Asesoria> crear(@RequestBody CrearAsesoriaReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(
                req.getProfesorId(),
                req.getAlumnoId(),
                req.getDisponibilidadId(),
                req.getMateria(),
                req.getObservaciones()
        ));
    }

    @PostMapping("/profesor")
    public ResponseEntity<Asesoria> crearPorProfesor(@RequestBody CrearAsesoriaPorProfesorReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearPorProfesor(
                req.getProfesorId(),
                req.getAlumnoId(),
                req.getFecha(),
                req.getHora(),
                req.getMateria(),
                req.getObservaciones()
        ));
    }

    @GetMapping
    public ResponseEntity<List<Asesoria>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Asesoria>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Asesoria>> porProfesor(@PathVariable Long profesorId) {
        return ResponseEntity.ok(service.porProfesor(profesorId));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Asesoria>> porAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(service.porAlumno(alumnoId));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Asesoria>> porFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.porFecha(LocalDate.parse(fecha)));
    }

    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Asesoria>> porEstatus(@PathVariable String estatus) {
        return ResponseEntity.ok(service.porEstatus(estatus));
    }

    @GetMapping("/profesor/{profesorId}/fecha/{fecha}")
    public ResponseEntity<List<Asesoria>> porProfesorYFecha(
            @PathVariable Long profesorId,
            @PathVariable String fecha) {
        return ResponseEntity.ok(service.porProfesorYFecha(profesorId, LocalDate.parse(fecha)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asesoria> actualizar(@PathVariable Long id, @RequestBody Asesoria asesoria) {
        return ResponseEntity.ok(service.actualizar(id, asesoria));
    }

    @PutMapping("/{id}/estatus/{nuevoEstatus}")
    public ResponseEntity<Asesoria> cambiarEstatus(
            @PathVariable Long id,
            @PathVariable String nuevoEstatus) {
        return ResponseEntity.ok(service.cambiarEstatus(id, nuevoEstatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CrearAsesoriaReq {
        private Long profesorId;
        private Long alumnoId;
        private Long disponibilidadId;
        private String materia;
        private String observaciones;
    }

    @Data
    public static class CrearAsesoriaPorProfesorReq {
        private Long profesorId;
        private Long alumnoId;
        private LocalDate fecha;
        private LocalTime hora;
        private String materia;
        private String observaciones;
    }
}

