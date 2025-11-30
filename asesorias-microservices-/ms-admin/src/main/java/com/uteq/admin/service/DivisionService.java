package com.uteq.admin.service;

import com.uteq.admin.dto.DivisionDTO;
import com.uteq.admin.entity.Division;
import java.util.List;
import java.util.Optional;

public interface DivisionService {
    Division crear(DivisionDTO dto);
    Division actualizar(Long id, DivisionDTO dto);
    Division obtenerPorId(Long id);
    List<Division> listarTodas();
    List<Division> listarActivas();
    void eliminar(Long id);
    void desactivar(Long id);
}
