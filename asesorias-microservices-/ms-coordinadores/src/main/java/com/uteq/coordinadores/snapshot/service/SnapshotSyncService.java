package com.uteq.coordinadores.snapshot.service;

import com.uteq.coordinadores.snapshot.client.AdminSnapshotClient;
import com.uteq.coordinadores.snapshot.client.DivisionSnapshotClient;
import com.uteq.coordinadores.snapshot.dto.DivisionDTO;
import com.uteq.coordinadores.snapshot.dto.ProgramaDTO;
import com.uteq.coordinadores.snapshot.dto.UsuarioDTO;
import com.uteq.coordinadores.snapshot.entity.MsAdminProfesor;
import com.uteq.coordinadores.snapshot.entity.MsAdminAlumno;
import com.uteq.coordinadores.snapshot.entity.MsDivision;
import com.uteq.coordinadores.snapshot.entity.MsPrograma;
import com.uteq.coordinadores.snapshot.repository.MsAdminProfesorRepository;
import com.uteq.coordinadores.snapshot.repository.MsAdminAlumnoRepository;
import com.uteq.coordinadores.snapshot.repository.MsDivisionRepository;
import com.uteq.coordinadores.snapshot.repository.MsProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotSyncService {

    private final AdminSnapshotClient adminClient;
    private final DivisionSnapshotClient divisionClient;

    private final MsAdminProfesorRepository profesorRepo;
    private final MsAdminAlumnoRepository alumnoRepo;
    private final MsDivisionRepository divisionRepo;
    private final MsProgramaRepository programaRepo;

    @Transactional
    public MsAdminProfesor syncProfesor(Long profesorId) {
        try {
            UsuarioDTO u = adminClient.obtenerUsuario(profesorId);
            return profesorRepo.save(MsAdminProfesor.builder()
                    .profesorId(profesorId)
                    .correoMatricula(u.getCorreoMatricula())
                    .nombre(u.getNombre())
                    .activo(u.getActivo())
                    .version(1L)
                    .build());
        } catch (Exception e) {
            return profesorRepo.findByProfesorId(profesorId);
        }
    }

    @Transactional
    public MsAdminAlumno syncAlumno(Long alumnoId) {
        try {
            UsuarioDTO u = adminClient.obtenerUsuario(alumnoId);
            return alumnoRepo.save(MsAdminAlumno.builder()
                    .alumnoId(alumnoId)
                    .correoMatricula(u.getCorreoMatricula())
                    .nombre(u.getNombre())
                    .activo(u.getActivo())
                    .version(1L)
                    .build());
        } catch (Exception e) {
            return alumnoRepo.findByAlumnoId(alumnoId);
        }
    }

    @Transactional
    public MsDivision syncDivision(Long divisionId) {
        try {
            DivisionDTO d = divisionClient.obtenerDivision(divisionId);
            return divisionRepo.save(MsDivision.builder()
                    .divisionId(divisionId)
                    .clave(d.getClave())
                    .nombre(d.getNombre())
                    .descripcion(d.getDescripcion())
                    .version(1L)
                    .build());
        } catch (Exception e) {
            return divisionRepo.findByDivisionId(divisionId);
        }
    }

    @Transactional
    public MsPrograma syncPrograma(Long programaId) {
        try {
            ProgramaDTO p = divisionClient.obtenerPrograma(programaId);
            return programaRepo.save(MsPrograma.builder()
                    .programaId(programaId)
                    .divisionId(p.getDivisionId())
                    .clave(p.getClave())
                    .nombre(p.getNombre())
                    .descripcion(p.getDescripcion())
                    .version(1L)
                    .build());
        } catch (Exception e) {
            return programaRepo.findByProgramaId(programaId);
        }
    }

    public MsAdminProfesor obtenerProfesor(Long profesorId) {
        return profesorRepo.findByProfesorId(profesorId);
    }

    public MsAdminAlumno obtenerAlumno(Long alumnoId) {
        return alumnoRepo.findByAlumnoId(alumnoId);
    }

    public List<MsDivision> listarDivisiones() {
        return divisionRepo.findAll();
    }

    public List<MsPrograma> listarProgramas() {
        return programaRepo.findAll();
    }
}
