package com.uteq.profesores.controller;

import com.uteq.profesores.client.AdminClient;
import com.uteq.profesores.client.AsesoriasClient;
import com.uteq.profesores.entity.Profesor;
import com.uteq.profesores.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final AdminClient admin;
    private final AsesoriasClient ases;
    private final ProfesorService profesorService;

    // 🔹 CRUD BÁSICO
    @PostMapping
    public Profesor crear(@RequestBody Profesor p) {
        return profesorService.crear(p);
    }

    @GetMapping
    public List<?> listar() {
        return profesorService.listar();
    }

    @GetMapping("/{id}")
    public Profesor obtener(@PathVariable Long id) {
        return profesorService.obtener(id);
    }

    @PutMapping("/{id}")
    public Profesor actualizar(@PathVariable Long id, @RequestBody Profesor p) {
        return profesorService.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
    }

    // 🔹 OPERACIONES DE NEGOCIO
    @GetMapping("/{usuarioId}/perfil")
    public Map<String, Object> perfil(@PathVariable Long usuarioId) {
        Map<String, Object> res = new HashMap<>();
        res.put("usuario", admin.usuario(usuarioId));
        res.put("perfil", admin.perfilProfesor(usuarioId));
        return res;
    }

    @GetMapping("/{usuarioId}/asesorias")
    public List<Map<String, Object>> asesorias(@PathVariable Long usuarioId) {
        return ases.asesoriasPorProfesor(usuarioId);
    }

    @PostMapping("/{usuarioId}/disponibilidades")
    public Map<String, Object> crearDisponibilidad(
            @PathVariable Long usuarioId,
            @RequestBody Map<String, Object> body) {
        body.put("profesorId", usuarioId);
        return ases.crearDisponibilidad(body);
    }

    @PostMapping("/{usuarioId}/asesorias")
    public Map<String, Object> crearAsesoria(
            @PathVariable Long usuarioId,
            @RequestBody Map<String, Object> body) {
        body.put("profesorId", usuarioId);
        return ases.crearAsesoriaPorProfesor(body);
    }
}
