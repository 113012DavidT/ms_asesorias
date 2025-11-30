package com.uteq.admin.controller;

import com.uteq.admin.dto.UsuarioDTO;
import com.uteq.admin.dto.UsuarioCrearDTO;
import com.uteq.admin.entity.Usuario;
import com.uteq.admin.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioCrearDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearDTO(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerDTO(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @RequestBody UsuarioCrearDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<UsuarioDTO> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        return ResponseEntity.ok(service.estado(id, activo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String password) {
        try {
            Usuario usuario = service.login(correo, password);
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(usuario.getId());
            dto.setCorreoMatricula(usuario.getCorreoMatricula());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setActivo(usuario.getActivo());
            
            if (usuario.getRol() != null) {
                dto.setRolId(usuario.getRol().getId());
                dto.setRolNombre(usuario.getRol().getNombre());
            } else {
                dto.setRolId(null);
                dto.setRolNombre("SIN_ROL");
            }
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + (e.getMessage() != null ? e.getMessage() : "Unknown") + "\"}");
        }
    }

    @PostMapping("/bootstrap")
    public ResponseEntity<?> bootstrapAdmin() {
        try {
            // Si ya existe, devolver OK
            List<UsuarioDTO> usuarios = service.listar();
            boolean existe = usuarios.stream().anyMatch(u -> "admin@uteq.edu".equalsIgnoreCase(u.getCorreoMatricula()));
            if (existe) {
                return ResponseEntity.ok("ADMIN ya existe");
            }
            // Crear ADMIN por defecto con rol ADMIN (id 1)
            UsuarioCrearDTO dto = UsuarioCrearDTO.builder()
                    .correoMatricula("admin@uteq.edu")
                    .password("pass123")
                    .nombre("Admin")
                    .apellido("UTeQ")
                    .rolId(1L)
                    .build();
            UsuarioDTO creado = service.crearDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error bootstrap: " + e.getMessage());
        }
    }

    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioDTO>> listarPorRol(@PathVariable Long rolId) {
        return ResponseEntity.ok(service.listarPorRol(rolId));
    }

    @GetMapping("/activos/listar")
    public ResponseEntity<List<UsuarioDTO>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }
}

