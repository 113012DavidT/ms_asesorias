package com.uteq.asesorias.service;

import com.uteq.asesorias.entity.Disponibilidad;
import com.uteq.asesorias.repository.DisponibilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository repo;

    @Override
    public Disponibilidad crear(Disponibilidad d) {
        return repo.save(d);
    }

    @Override
    public List<Disponibilidad> porProfesor(Long profesorId) {
        return repo.findByProfesorId(profesorId);
    }

    @Override
    public List<Disponibilidad> porProfesorYFecha(Long profesorId, LocalDate fecha) {
        return repo.findByProfesorIdAndFecha(profesorId, fecha);
    }

    @Override
    public List<Disponibilidad> disponibles() {
        return repo.findByDisponibleTrue();
    }

    @Override
    public List<Disponibilidad> nodisponibles() {
        return repo.findByDisponibleFalse();
    }

    @Override
    public Optional<Disponibilidad> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Disponibilidad> listarTodas() {
        return repo.findAll();
    }

    @Override
    public Disponibilidad actualizar(Long id, Disponibilidad disponibilidad) {
        Disponibilidad existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
        
        existente.setFecha(disponibilidad.getFecha());
        existente.setHoraInicio(disponibilidad.getHoraInicio());
        existente.setHoraFin(disponibilidad.getHoraFin());
        
        return repo.save(existente);
    }

    @Override
    public Disponibilidad marcarDisponible(Long id, Boolean disponible) {
        Disponibilidad disp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
        
        disp.setDisponible(disponible);
        return repo.save(disp);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}

