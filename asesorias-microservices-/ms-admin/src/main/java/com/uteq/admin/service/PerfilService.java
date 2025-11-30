package com.uteq.admin.service;

import com.uteq.admin.entity.AlumnoPerfil;
import com.uteq.admin.entity.ProfesorPerfil;
import java.util.List;

public interface PerfilService {

    ProfesorPerfil guardarProfesor(ProfesorPerfil p);
    AlumnoPerfil guardarAlumno(AlumnoPerfil p);

    ProfesorPerfil porProfesorUsuario(Long usuarioId);
    AlumnoPerfil porAlumnoUsuario(Long usuarioId);

    // ✅ Nuevos métodos para listar todos
    List<ProfesorPerfil> listarProfesores();
    List<AlumnoPerfil> listarAlumnos();
}
