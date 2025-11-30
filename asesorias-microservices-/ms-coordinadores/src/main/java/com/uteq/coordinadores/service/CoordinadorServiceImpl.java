package com.uteq.coordinadores.service;

import com.uteq.coordinadores.client.AdminClient;
import com.uteq.coordinadores.client.DivisionClient;
import com.uteq.coordinadores.entity.Asignacion;
import com.uteq.coordinadores.repository.AsignacionRepository;
import com.uteq.coordinadores.snapshot.service.SnapshotSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoordinadorServiceImpl implements CoordinadorService {

    private final AdminClient adminClient;
    private final DivisionClient divisionClient;
    private final AsignacionRepository asignacionRepo;
    private final SnapshotSyncService snapshotService;

    @Override
    @Transactional
    public Map<String, Object> asignarProfesor(Long usuarioId, Map<String, Object> body) {
        if (!body.containsKey("divisionId") || !body.containsKey("programaId")) {
            throw new RuntimeException("Faltan campos: divisionId o programaId");
        }

        Long divisionId = Long.valueOf(body.get("divisionId").toString());
        Long programaId = Long.valueOf(body.get("programaId").toString());

        // Sincronizar snapshot del profesor
        snapshotService.syncProfesor(usuarioId);
        snapshotService.syncDivision(divisionId);
        snapshotService.syncPrograma(programaId);

        // Guardar asignación local
        Asignacion asignacion = Asignacion.builder()
                .usuarioId(usuarioId)
                .divisionId(divisionId)
                .programaId(programaId)
                .rol("profesor")
                .activo(true)
                .build();
        asignacionRepo.save(asignacion);

        // Actualizar en ms-admin también
        Map<String, Object> perfil = new HashMap<>();
        perfil.put("usuarioId", usuarioId);
        perfil.put("divisionId", divisionId);
        perfil.put("programaId", programaId);

        Map<String, Object> res = new HashMap<>();
        res.put("asignacion", asignacion);
        res.put("mensaje", "Profesor asignado exitosamente");
        return res;
    }

    @Override
    @Transactional
    public Map<String, Object> asignarAlumno(Long usuarioId, Map<String, Object> body) {
        if (!body.containsKey("divisionId") || !body.containsKey("programaId")) {
            throw new RuntimeException("Faltan campos: divisionId o programaId");
        }

        Long divisionId = Long.valueOf(body.get("divisionId").toString());
        Long programaId = Long.valueOf(body.get("programaId").toString());

        // Sincronizar snapshot del alumno
        snapshotService.syncAlumno(usuarioId);
        snapshotService.syncDivision(divisionId);
        snapshotService.syncPrograma(programaId);

        // Guardar asignación local
        Asignacion asignacion = Asignacion.builder()
                .usuarioId(usuarioId)
                .divisionId(divisionId)
                .programaId(programaId)
                .rol("alumno")
                .activo(true)
                .build();
        asignacionRepo.save(asignacion);

        // Actualizar en ms-admin también
        Map<String, Object> perfil = new HashMap<>();
        perfil.put("usuarioId", usuarioId);
        perfil.put("divisionId", divisionId);
        perfil.put("programaId", programaId);

        Map<String, Object> res = new HashMap<>();
        res.put("asignacion", asignacion);
        res.put("mensaje", "Alumno asignado exitosamente");
        return res;
    }

    @Override
    public List<Map<String, Object>> listarProfesores() {
        List<Asignacion> profesores = asignacionRepo.findByRol("profesor");
        return profesores.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("asignacion", p);
            m.put("snapshot", snapshotService.obtenerProfesor(p.getUsuarioId()));
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> listarAlumnos() {
        List<Asignacion> alumnos = asignacionRepo.findByRol("alumno");
        return alumnos.stream().map(a -> {
            Map<String, Object> m = new HashMap<>();
            m.put("asignacion", a);
            m.put("snapshot", snapshotService.obtenerAlumno(a.getUsuarioId()));
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> listarDivisionesYProgramas() {
        Map<String, Object> res = new HashMap<>();
        res.put("divisiones", snapshotService.listarDivisiones());
        res.put("programas", snapshotService.listarProgramas());
        return res;
    }
}
