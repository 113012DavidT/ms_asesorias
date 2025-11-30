package com.uteq.admin.controller;

import com.uteq.admin.entity.AlumnoPerfil;
import com.uteq.admin.entity.ProfesorPerfil;
import com.uteq.admin.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService service;

    // 🔹 Crear perfil de profesor
    @PostMapping("/profesor")
    public ProfesorPerfil guardarProfesor(@RequestBody ProfesorPerfil p) {
        return service.guardarProfesor(p);
    }

    // 🔹 Crear perfil de alumno
    @PostMapping("/alumno")
    public AlumnoPerfil guardarAlumno(@RequestBody AlumnoPerfil p) {
        return service.guardarAlumno(p);
    }

    // 🔹 Obtener perfil de profesor por ID de usuario
    @GetMapping("/profesor/{usuarioId}")
    public ProfesorPerfil porProfesor(@PathVariable Long usuarioId) {
        return service.porProfesorUsuario(usuarioId);
    }

    // 🔹 Obtener perfil de alumno por ID de usuario
    @GetMapping("/alumno/{usuarioId}")
    public AlumnoPerfil porAlumno(@PathVariable Long usuarioId) {
        return service.porAlumnoUsuario(usuarioId);
    }

    // ✅ NUEVOS MÉTODOS: Listar todos los perfiles
    @GetMapping("/profesor")
    public List<ProfesorPerfil> listarProfesores() {
        return service.listarProfesores();
    }

    @GetMapping("/alumno")
    public List<AlumnoPerfil> listarAlumnos() {
        return service.listarAlumnos();
    }
}
