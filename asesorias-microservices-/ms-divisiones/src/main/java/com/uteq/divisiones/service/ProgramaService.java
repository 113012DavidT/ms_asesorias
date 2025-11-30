package com.uteq.divisiones.service;

import com.uteq.divisiones.entity.ProgramaEducativo;
import java.util.List;

public interface ProgramaService {
    ProgramaEducativo crear(ProgramaEducativo p);
    List<ProgramaEducativo> listar();
    ProgramaEducativo obtener(Long id);
    ProgramaEducativo actualizar(Long id, ProgramaEducativo p);
    void eliminar(Long id);
    List<ProgramaEducativo> listarPorDivision(Long divisionId);
}
