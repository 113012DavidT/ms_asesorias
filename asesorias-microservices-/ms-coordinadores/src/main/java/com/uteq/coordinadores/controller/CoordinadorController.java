package com.uteq.coordinadores.controller;

import com.uteq.coordinadores.service.CoordinadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coordinadores")
@RequiredArgsConstructor
public class CoordinadorController {

    private final CoordinadorService service;

    // 🔹 Asignar profesor a división y programa
    @PutMapping("/asignar/profesor/{usuarioId}")
    public Map<String, Object> asignarProfesor(@PathVariable Long usuarioId, @RequestBody Map<String, Object> body) {
        return service.asignarProfesor(usuarioId, body);
    }

    // 🔹 Asignar alumno a división y programa
    @PutMapping("/asignar/alumno/{usuarioId}")
    public Map<String, Object> asignarAlumno(@PathVariable Long usuarioId, @RequestBody Map<String, Object> body) {
        return service.asignarAlumno(usuarioId, body);
    }

    // 🔹 Listar todos los profesores
    @GetMapping("/profesores")
    public List<Map<String, Object>> listarProfesores() {
        return service.listarProfesores();
    }

    // 🔹 Listar todos los alumnos
    @GetMapping("/alumnos")
    public List<Map<String, Object>> listarAlumnos() {
        return service.listarAlumnos();
    }

    // 🔹 Listar divisiones y programas disponibles
    @GetMapping("/divisiones-programas")
    public Map<String, Object> listarDivisionesYProgramas() {
        return service.listarDivisionesYProgramas();
    }
}
