package com.uteq.alumnos.controller;

import com.uteq.alumnos.client.AdminClient;
import com.uteq.alumnos.client.AsesoriasClient;
import com.uteq.alumnos.entity.Alumno;
import com.uteq.alumnos.service.AlumnoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    Map<String, Object> usuario = admin.usuario(usuarioId);
    res.put("usuario", usuario);
    // Construir perfil desde entidad local (evitar dependencia estricta de ms-admin perfiles)
    Alumno a = alumnoService.obtenerPorUsuario(usuarioId);
    if (a != null) {
      Map<String, Object> perfil = new HashMap<>();
      perfil.put("usuarioId", usuarioId);
      Map<String, Object> division = new HashMap<>();
      division.put("id", a.getDivisionId());
      perfil.put("division", division);
      Map<String, Object> programa = new HashMap<>();
      programa.put("id", a.getProgramaId());
      perfil.put("programa", programa);
      perfil.put("activo", a.getActivo());
      res.put("perfil", perfil);
    } else {
      res.put("perfil", null);
    }
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

  // 🔹 CANDIDATOS (usuarios con rol ALUMNO desde ms-admin)
  @GetMapping("/candidatos")
  public List<Map<String, Object>> candidatosAlumnos(
      @RequestHeader(value = "X-User-Role", required = false) String role
  ) {
    if (role == null || role.isBlank()) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Falta rol");
    }
    String r = role.trim().toUpperCase();
    if (!("COORDINADOR".equals(r) || "ADMIN".equals(r))) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Rol no autorizado");
    }
    Long rolId = null;
    for (Map<String, Object> rol : admin.roles()) {
      String nombre = String.valueOf(rol.getOrDefault("nombre", ""));
      if ("ALUMNO".equalsIgnoreCase(nombre)) {
        Object id = rol.get("id");
        if (id != null) rolId = Long.valueOf(String.valueOf(id));
        break;
      }
    }
    if (rolId == null) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Rol ALUMNO no encontrado");
    }
    return admin.usuariosPorRol(rolId);
  }

  // 🔹 ASIGNAR ALUMNOS A DIVISIÓN/PROGRAMA (bulk)
  @PostMapping("/asignar")
  public Map<String, Object> asignarAlumnos(
      @RequestHeader(value = "X-User-Role", required = false) String role,
      @RequestBody AsignarReq req) {
    if (role == null || role.isBlank()) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Falta rol");
    }
    String r = role.trim().toUpperCase();
    if (!("COORDINADOR".equals(r) || "ADMIN".equals(r))) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Rol no autorizado");
    }
    if (req == null || req.getDivisionId() == null || req.getProgramaId() == null || req.getUsuarioIds() == null) {
      throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Parámetros inválidos");
    }

    List<Long> procesados = new ArrayList<>();
    List<Long> yaAsignados = new ArrayList<>();
    for (Long usuarioId : req.getUsuarioIds()) {
      if (usuarioId == null) continue;
      Alumno existente = alumnoService.obtenerPorUsuario(usuarioId);
      if (existente != null) {
        // Ya asignado: no reasignar en bulk; se debe editar explícitamente
        yaAsignados.add(usuarioId);
      } else {
        Map<String, Object> usuario = admin.usuario(usuarioId);
        String nombre = usuario != null ? String.valueOf(usuario.getOrDefault("nombre", "")) : "";
        String apellido = usuario != null ? String.valueOf(usuario.getOrDefault("apellido", "")) : "";
        String correo = usuario != null ? String.valueOf(usuario.getOrDefault("correoMatricula", "")) : "";
        Alumno nuevo = Alumno.builder()
            .usuarioId(usuarioId)
            .divisionId(req.getDivisionId())
            .programaId(req.getProgramaId())
            .nombre((nombre + " " + apellido).trim())
            .correoMatricula(correo)
            .activo(true)
            .build();
        alumnoService.crear(nuevo);
        procesados.add(usuarioId);
      }
    }
    Map<String, Object> res = new HashMap<>();
    res.put("ok", true);
    res.put("creados", procesados.size());
    res.put("creadosUsuarioIds", procesados);
    res.put("yaAsignados", yaAsignados);
    return res;
  }

  @Data
  public static class CrearReq {
    private Long profesorId;
    private Long disponibilidadId;
    private String materia;
    private String observaciones;
  }

  @Data
  public static class AsignarReq {
    private List<Long> usuarioIds;
    private Long divisionId;
    private Long programaId;
  }
}
