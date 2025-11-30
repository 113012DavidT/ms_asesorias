package com.uteq.divisiones.service;

import com.uteq.divisiones.entity.ProgramaEducativo;
import com.uteq.divisiones.repository.ProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepo;

    @Override
    @Transactional
    public ProgramaEducativo crear(ProgramaEducativo p) {
        // Generar clave automáticamente si no se proporciona
        if (p.getClave() == null || p.getClave().trim().isEmpty()) {
            String clave = "PROG-" + System.currentTimeMillis();
            p.setClave(clave);
        }
        return programaRepo.save(p);
    }

    @Override
    public List<ProgramaEducativo> listar() {
        return programaRepo.findAll();
    }

    @Override
    public ProgramaEducativo obtener(Long id) {
        return programaRepo.findById(id).orElseThrow(() -> new RuntimeException("Programa no encontrado"));
    }

    @Override
    @Transactional
    public ProgramaEducativo actualizar(Long id, ProgramaEducativo p) {
        ProgramaEducativo programa = obtener(id);
        if (p.getNombre() != null) programa.setNombre(p.getNombre());
        if (p.getClave() != null) programa.setClave(p.getClave());
        if (p.getDescripcion() != null) programa.setDescripcion(p.getDescripcion());
        return programaRepo.save(programa);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        programaRepo.deleteById(id);
    }

    @Override
    public List<ProgramaEducativo> listarPorDivision(Long divisionId) {
        return programaRepo.findByDivisionId(divisionId);
    }
}
