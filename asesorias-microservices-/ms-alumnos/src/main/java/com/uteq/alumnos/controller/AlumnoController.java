package com.uteq.alumnos.controller;

import com.uteq.alumnos.client.AdminClient;
import com.uteq.alumnos.client.AsesoriasClient;
import com.uteq.alumnos.entity.Alumno;
import com.uteq.alumnos.service.AlumnoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

  private final AdminClient admin;
  private final AsesoriasClient ases;
  private final AlumnoService alumnoService;

  // 🔹 CRUD BÁSICO
  @PostMapping
  public Alumno crear(@RequestBody Alumno a) {
    return alumnoService.crear(a);
  }

  @GetMapping
  public List<?> listar() {
    return alumnoService.listar();
  }

  @GetMapping("/{id}")
  public Alumno obtener(@PathVariable Long id) {
    return alumnoService.obtener(id);
  }

  @PutMapping("/{id}")
  public Alumno actualizar(@PathVariable Long id, @RequestBody Alumno a) {
    return alumnoService.actualizar(id, a);
  }

  @DeleteMapping("/{id}")
  public void eliminar(@PathVariable Long id) {
    alumnoService.eliminar(id);
  }

  // 🔹 OPERACIONES DE NEGOCIO
  @GetMapping("/{usuarioId}/perfil")
  public Map<String, Object> perfil(@PathVariable Long usuarioId) {
    Map<String, Object> res = new HashMap<>();
    res.put("usuario", admin.usuario(usuarioId));
    res.put("perfil", admin.perfilAlumno(usuarioId));
    return res;
  }

  @GetMapping("/{usuarioId}/asesorias")
  public List<Map<String, Object>> asesorias(@PathVariable Long usuarioId) {
    return ases.asesoriasPorAlumno(usuarioId);
  }

  @PostMapping("/{usuarioId}/asesorias")
  public Map<String, Object> crearAsesoria(@PathVariable Long usuarioId, @RequestBody CrearReq req) {
    Map<String, Object> body = new HashMap<>();
    body.put("profesorId", req.getProfesorId());
    body.put("alumnoId", usuarioId);
    body.put("disponibilidadId", req.getDisponibilidadId());
    body.put("materia", req.getMateria());
    body.put("observaciones", req.getObservaciones());
    return ases.crearAsesoria(body);
  }

  @Data
  public static class CrearReq {
    private Long profesorId;
    private Long disponibilidadId;
    private String materia;
    private String observaciones;
  }
}
