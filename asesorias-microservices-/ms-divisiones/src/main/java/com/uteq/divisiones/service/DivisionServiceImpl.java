package com.uteq.divisiones.service;

import com.uteq.divisiones.entity.Division;
import com.uteq.divisiones.repository.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {

    private final DivisionRepository divisionRepo;

    @Override
    @Transactional
    public Division crear(Division d) {
        // Generar clave automáticamente si no se proporciona
        if (d.getClave() == null || d.getClave().trim().isEmpty()) {
            String clave = "DIV-" + System.currentTimeMillis();
            d.setClave(clave);
        }
        return divisionRepo.save(d);
    }

    @Override
    public List<Division> listar() {
        return divisionRepo.findAll();
    }

    @Override
    public Division obtener(Long id) {
        return divisionRepo.findById(id).orElseThrow(() -> new RuntimeException("División no encontrada"));
    }

    @Override
    @Transactional
    public Division actualizar(Long id, Division d) {
        Division division = obtener(id);
        if (d.getNombre() != null) division.setNombre(d.getNombre());
        if (d.getClave() != null) division.setClave(d.getClave());
        if (d.getDescripcion() != null) division.setDescripcion(d.getDescripcion());
        return divisionRepo.save(division);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        divisionRepo.deleteById(id);
    }
}
