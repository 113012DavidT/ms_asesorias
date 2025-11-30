package com.uteq.admin.service;

import com.uteq.admin.entity.AlumnoPerfil;
import com.uteq.admin.entity.ProfesorPerfil;
import com.uteq.admin.repository.AlumnoPerfilRepository;
import com.uteq.admin.repository.ProfesorPerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final ProfesorPerfilRepository profRepo;
    private final AlumnoPerfilRepository alumRepo;

    @Override
    public ProfesorPerfil guardarProfesor(ProfesorPerfil p) {
        return profRepo.findByUsuarioId(p.getUsuarioId())
                .map(existente -> {
                    existente.setDivision(p.getDivision());
                    existente.setPrograma(p.getPrograma());
                    return profRepo.save(existente);
                })
                .orElseGet(() -> profRepo.save(p));
    }

    @Override
    public AlumnoPerfil guardarAlumno(AlumnoPerfil p) {
        return alumRepo.findByUsuarioId(p.getUsuarioId())
                .map(existente -> {
                    existente.setDivision(p.getDivision());
                    existente.setPrograma(p.getPrograma());
                    return alumRepo.save(existente);
                })
                .orElseGet(() -> alumRepo.save(p));
    }

    @Override
    public ProfesorPerfil porProfesorUsuario(Long usuarioId) {
        return profRepo.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado para usuarioId: " + usuarioId));
    }

    @Override
    public AlumnoPerfil porAlumnoUsuario(Long usuarioId) {
        return alumRepo.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Perfil de alumno no encontrado para usuarioId: " + usuarioId));
    }

    // ✅ Nuevos métodos para listar todos los perfiles
    @Override
    public List<ProfesorPerfil> listarProfesores() {
        return profRepo.findAll();
    }

    @Override
    public List<AlumnoPerfil> listarAlumnos() {
        return alumRepo.findAll();
    }
}
