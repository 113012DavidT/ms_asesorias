package com.uteq.profesores.controller;

import com.uteq.profesores.client.AdminClient;
import com.uteq.profesores.client.AsesoriasClient;
import com.uteq.profesores.client.AlumnosClient;
import com.uteq.profesores.entity.Profesor;
import com.uteq.profesores.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final AlumnosClient alumnosClient;

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
        Map<String, Object> usuario = admin.usuario(usuarioId);
        res.put("usuario", usuario);
        // Construir perfil desde entidad local (evitar dependencia estricta de ms-admin perfiles)
        Profesor p = profesorService.obtenerPorUsuario(usuarioId);
        if (p != null) {
            Map<String, Object> perfil = new HashMap<>();
            perfil.put("usuarioId", usuarioId);
            Map<String, Object> division = new HashMap<>();
            division.put("id", p.getDivisionId());
            perfil.put("division", division);
            Map<String, Object> programa = new HashMap<>();
            programa.put("id", p.getProgramaId());
            perfil.put("programa", programa);
            perfil.put("activo", p.getActivo());
            res.put("perfil", perfil);
        } else {
            res.put("perfil", null);
        }
        return res;
    }

    // 🔹 Listado curado: alumnos del profesor (según su división y programa)
    @GetMapping("/mis-alumnos")
    public List<Map<String, Object>> misAlumnos(
            @RequestHeader(value = "X-User-Id", required = false) String xUserId
    ) {
        if (xUserId == null || xUserId.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Falta X-User-Id");
        }
        Long usuarioId;
        try { usuarioId = Long.valueOf(xUserId.trim()); } catch (NumberFormatException e) { throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "X-User-Id inválido"); }

        Profesor p = profesorService.obtenerPorUsuario(usuarioId);
        if (p == null || p.getDivisionId() == null || p.getProgramaId() == null) {
            // Fallback: si el profesor aún no está asignado, devolver listado completo
            // para evitar romper la UI de profesor y permitir selección/gestión básica.
            return alumnosClient.listar();
        }
        Long divId = p.getDivisionId();
        Long progId = p.getProgramaId();

        List<Map<String, Object>> all = alumnosClient.listar();
        List<Map<String, Object>> filtrados = new java.util.ArrayList<>();
        for (Map<String, Object> a : all) {
            Object d = a.get("divisionId");
            Object pr = a.get("programaId");
            Long dId = d == null ? null : Long.valueOf(String.valueOf(d));
            Long pId = pr == null ? null : Long.valueOf(String.valueOf(pr));
            if (divId.equals(dId) && progId.equals(pId)) {
                filtrados.add(a);
            }
        }
        return filtrados;
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

    // 🔹 ASIGNAR PROFESORES A DIVISIÓN/PROGRAMA (bulk)
    @PostMapping("/asignar")
    public Map<String, Object> asignarProfesores(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody AsignarRequest req) {
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
            Profesor existente = profesorService.obtenerPorUsuario(usuarioId);
            if (existente != null) {
                // Ya asignado: no reasignar en bulk; se debe editar explícitamente
                yaAsignados.add(usuarioId);
            } else {
                // Obtener datos del usuario desde ms-admin
                Map<String, Object> usuario = admin.usuario(usuarioId);
                String nombre = usuario != null ? String.valueOf(usuario.getOrDefault("nombre", "")) : "";
                String apellido = usuario != null ? String.valueOf(usuario.getOrDefault("apellido", "")) : "";
                String correo = usuario != null ? String.valueOf(usuario.getOrDefault("correoMatricula", "")) : "";
                Profesor nuevo = Profesor.builder()
                        .usuarioId(usuarioId)
                        .divisionId(req.getDivisionId())
                        .programaId(req.getProgramaId())
                        .nombre((nombre + " " + apellido).trim())
                        .correoMatricula(correo)
                        .activo(true)
                        .build();
                profesorService.crear(nuevo);
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

    // 🔹 CANDIDATOS (usuarios con rol PROFESOR desde ms-admin)
    @GetMapping("/candidatos")
    public List<Map<String, Object>> candidatosProfesores(
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {
        if (role == null || role.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Falta rol");
        }
        String r = role.trim().toUpperCase();
        if (!("COORDINADOR".equals(r) || "ADMIN".equals(r))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Rol no autorizado");
        }
        // Buscar rolId de PROFESOR
        Long rolId = null;
        for (Map<String, Object> rol : admin.roles()) {
            String nombre = String.valueOf(rol.getOrDefault("nombre", ""));
            if ("PROFESOR".equalsIgnoreCase(nombre)) {
                Object id = rol.get("id");
                if (id != null) rolId = Long.valueOf(String.valueOf(id));
                break;
            }
        }
        if (rolId == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Rol PROFESOR no encontrado");
        }
        return admin.usuariosPorRol(rolId);
    }

    public static class AsignarRequest {
        private List<Long> usuarioIds;
        private Long divisionId;
        private Long programaId;

        public List<Long> getUsuarioIds() { return usuarioIds; }
        public void setUsuarioIds(List<Long> usuarioIds) { this.usuarioIds = usuarioIds; }
        public Long getDivisionId() { return divisionId; }
        public void setDivisionId(Long divisionId) { this.divisionId = divisionId; }
        public Long getProgramaId() { return programaId; }
        public void setProgramaId(Long programaId) { this.programaId = programaId; }
    }
}
