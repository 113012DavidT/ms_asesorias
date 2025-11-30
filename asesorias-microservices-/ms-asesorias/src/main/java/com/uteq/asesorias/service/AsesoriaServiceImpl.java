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

@Service
@RequiredArgsConstructor
public class AsesoriaServiceImpl implements AsesoriaService {

    private final AsesoriaRepository repo;
    private final DisponibilidadRepository dispRepo;
    private final AdminClient admin;

    @Override
    public Asesoria crear(Long profesorId, Long alumnoId, Long disponibilidadId, String materia, String observaciones) {
        // 1️⃣ Validar perfiles: mismo programa
        Map<String, Object> profPerfil = admin.perfilProfesor(profesorId);
        Map<String, Object> alumPerfil = admin.perfilAlumno(alumnoId);

        if (profPerfil == null || alumPerfil == null)
            throw new RuntimeException("Perfiles no encontrados");

        Long profPrograma = profPerfil.get("programaId") == null ? null :
                Long.valueOf(profPerfil.get("programaId").toString());
        Long alumPrograma = alumPerfil.get("programaId") == null ? null :
                Long.valueOf(alumPerfil.get("programaId").toString());

        if (profPrograma == null || alumPrograma == null || !profPrograma.equals(alumPrograma))
            throw new RuntimeException("Alumno y Profesor no pertenecen al mismo programa");

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
                .materia(materia)
                .observaciones(observaciones)
                .estatus("CREADA")
                .fechaRegistro(LocalDate.now())
                .build();

        return repo.save(a);
    }

    @Override
    public Asesoria crearPorProfesor(Long profesorId, Long alumnoId,
                                     LocalDate fecha, LocalTime hora,
                                     String materia, String observaciones) {

        // 1️⃣ Validar perfiles
        Map<String, Object> profPerfil = admin.perfilProfesor(profesorId);
        Map<String, Object> alumPerfil = admin.perfilAlumno(alumnoId);

        if (profPerfil == null || alumPerfil == null)
            throw new RuntimeException("Perfiles no encontrados");

        Long profPrograma = profPerfil.get("programaId") == null ? null :
                Long.valueOf(profPerfil.get("programaId").toString());
        Long alumPrograma = alumPerfil.get("programaId") == null ? null :
                Long.valueOf(alumPerfil.get("programaId").toString());

        if (profPrograma == null || alumPrograma == null || !profPrograma.equals(alumPrograma))
            throw new RuntimeException("Alumno y Profesor no pertenecen al mismo programa");

        // 2️⃣ Buscar disponibilidad activa del profesor en esa fecha/hora
        Disponibilidad disp = dispRepo
                .findByProfesorIdAndFechaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualAndDisponibleTrue(
                        profesorId, fecha, hora, hora)
                .orElseThrow(() -> new RuntimeException("No hay disponibilidad activa en esa fecha y hora"));

        // 3️⃣ Crear asesoría y marcar disponibilidad ocupada
        disp.setDisponible(false);
        dispRepo.save(disp);

        Asesoria nueva = Asesoria.builder()
                .profesorId(profesorId)
                .alumnoId(alumnoId)
                .disponibilidadId(disp.getId())
                .fecha(fecha)
                .hora(hora)
                .materia(materia)
                .observaciones(observaciones)
                .estatus("ASIGNADA")
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
}

