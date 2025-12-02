package com.uteq.asesorias.service;

import com.uteq.asesorias.client.AdminClient;
import com.uteq.asesorias.entity.Asesoria;
import com.uteq.asesorias.entity.Disponibilidad;
import com.uteq.asesorias.repository.AsesoriaRepository;
import com.uteq.asesorias.repository.DisponibilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import feign.FeignException;

@Service
@RequiredArgsConstructor
public class AsesoriaServiceImpl implements AsesoriaService {

    private final AsesoriaRepository repo;
    private final DisponibilidadRepository dispRepo;
    private final AdminClient admin;

    @Override
    public Asesoria crear(Long profesorId, Long alumnoId, Long disponibilidadId, String materia, String observaciones) {
        // 1️⃣ Validar perfiles: mismo programa
        Map<String, Object> profPerfil;
        Map<String, Object> alumPerfil;
        try {
            profPerfil = admin.perfilProfesor(profesorId);
            alumPerfil = admin.perfilAlumno(alumnoId);
        } catch (FeignException e) {
            throw new IllegalArgumentException("No se pudo obtener perfiles en ms-admin: " + e.status());
        }

        if (profPerfil == null || alumPerfil == null)
            throw new IllegalArgumentException("Perfiles no encontrados");

        Long profPrograma = extraerProgramaId(profPerfil);
        Long alumPrograma = extraerProgramaId(alumPerfil);

        if (profPrograma == null || alumPrograma == null || !profPrograma.equals(alumPrograma))
            throw new IllegalArgumentException("Alumno y Profesor no pertenecen al mismo programa");

        // 2️⃣ Validar disponibilidad
        Disponibilidad d = dispRepo.findById(disponibilidadId)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        if (!Boolean.TRUE.equals(d.getDisponible()))
            throw new RuntimeException("Horario no disponible");

        if (!d.getProfesorId().equals(profesorId))
            throw new RuntimeException("La disponibilidad no corresponde al profesor");

        // 3️⃣ Reservar: marcar slot ocupado y crear asesoría
        d.setDisponible(false);
        dispRepo.save(d);

        Asesoria a = Asesoria.builder()
                .profesorId(profesorId)
                .alumnoId(alumnoId)
                .disponibilidadId(disponibilidadId)
                .fecha(d.getFecha())
            .hora(d.getHoraInicio())
            .horaInicio(d.getHoraInicio())
            .horaFin(d.getHoraFin())
            .titulo(materia)
                .materia(materia)
                .observaciones(observaciones)
                .estatus("CREADA")
                .fechaRegistro(LocalDate.now())
                .build();

        return repo.save(a);
    }

    @Override
    public Asesoria crearPorProfesor(Long profesorId, Long alumnoId,
                                     LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                                     String titulo, String materia, String observaciones) {

        // 1️⃣ Validar perfiles (tolerante): si no se puede obtener o faltan programas, permitimos continuar
        try {
            Map<String, Object> profPerfil = admin.perfilProfesor(profesorId);
            Map<String, Object> alumPerfil = admin.perfilAlumno(alumnoId);
            Long profPrograma = extraerProgramaId(profPerfil);
            Long alumPrograma = extraerProgramaId(alumPerfil);
            if (profPrograma != null && alumPrograma != null && !profPrograma.equals(alumPrograma)) {
                throw new IllegalArgumentException("Alumno y Profesor no pertenecen al mismo programa");
            }
        } catch (FeignException e) {
            // Continuar sin bloquear la solicitud; el profesor podrá revisar/aceptar
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ignored) {
            // Cualquier otro caso: continuar
        }

        // 2️⃣ Crear solicitud libre (sin exigir disponibilidad), queda en PENDIENTE para revisión
        Asesoria nueva = Asesoria.builder()
                .profesorId(profesorId)
                .alumnoId(alumnoId)
                .disponibilidadId(null)
                .fecha(fecha)
            .hora(horaInicio)
            .horaInicio(horaInicio)
            .horaFin(horaFin)
            .titulo(titulo != null && !titulo.isBlank() ? titulo : materia)
                .materia(materia)
                .observaciones(observaciones)
                .estatus("PENDIENTE")
                .fechaRegistro(LocalDate.now())
                .build();

        return repo.save(nueva);
    }

    @Override
    public List<Asesoria> porProfesor(Long profesorId) {
        return repo.findByProfesorId(profesorId);
    }

    @Override
    public List<Asesoria> porAlumno(Long alumnoId) {
        return repo.findByAlumnoId(alumnoId);
    }

    @Override
    public Optional<Asesoria> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Asesoria> listarTodas() {
        return repo.findAll();
    }

    @Override
    public Asesoria actualizar(Long id, Asesoria asesoria) {
        Asesoria existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asesoría no encontrada"));
        
        existente.setMateria(asesoria.getMateria());
        existente.setObservaciones(asesoria.getObservaciones());
        
        return repo.save(existente);
    }

    @Override
    public Asesoria cambiarEstatus(Long id, String nuevoEstatus) {
        Asesoria asesoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asesoría no encontrada"));
        
        asesoria.setEstatus(nuevoEstatus);
        return repo.save(asesoria);
    }

    @Override
    public Asesoria asignarDisponibilidad(Long id, Long disponibilidadId) {
        Asesoria asesoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asesoría no encontrada"));

        Disponibilidad disp = dispRepo.findById(disponibilidadId)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        if (!Boolean.TRUE.equals(disp.getDisponible()))
            throw new IllegalArgumentException("La disponibilidad ya está ocupada");

        if (asesoria.getProfesorId() != null && !asesoria.getProfesorId().equals(disp.getProfesorId()))
            throw new IllegalArgumentException("La disponibilidad no corresponde al profesor de la asesoría");

        // Asignar datos de la disponibilidad a la asesoría
        asesoria.setProfesorId(disp.getProfesorId());
        asesoria.setDisponibilidadId(disp.getId());
        asesoria.setFecha(disp.getFecha());
        asesoria.setHora(disp.getHoraInicio());
        asesoria.setHoraInicio(disp.getHoraInicio());
        asesoria.setHoraFin(disp.getHoraFin());
        asesoria.setEstatus("PROGRAMADA");

        // Marcar disponibilidad como ocupada
        disp.setDisponible(false);
        dispRepo.save(disp);

        return repo.save(asesoria);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Asesoria> porFecha(LocalDate fecha) {
        return repo.findByFecha(fecha);
    }

    @Override
    public List<Asesoria> porEstatus(String estatus) {
        return repo.findByEstatus(estatus);
    }

    @Override
    public List<Asesoria> porProfesorYFecha(Long profesorId, LocalDate fecha) {
        return repo.findByProfesorIdAndFecha(profesorId, fecha);
    }

    private Long extraerProgramaId(Map<String, Object> perfil) {
        if (perfil == null) return null;
        Object programaId = perfil.get("programaId");
        if (programaId != null) {
            try { return Long.valueOf(programaId.toString()); } catch (NumberFormatException ignored) {}
        }
        Object programaObj = perfil.get("programa");
        if (programaObj instanceof Map<?, ?> m) {
            Object id = m.get("id");
            if (id != null) {
                try { return Long.valueOf(id.toString()); } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }
}

