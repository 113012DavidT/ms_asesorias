package com.uteq.admin.service;

import com.uteq.admin.dto.ProgramaDTO;
import com.uteq.admin.entity.Programa;
import java.util.List;
import java.util.Optional;

public interface ProgramaService {
    Programa crear(ProgramaDTO dto);
    Programa actualizar(Long id, ProgramaDTO dto);
    Programa obtenerPorId(Long id);
    List<Programa> listarTodas();
    List<Programa> listarActivos();
    void eliminar(Long id);
    void desactivar(Long id);
}
