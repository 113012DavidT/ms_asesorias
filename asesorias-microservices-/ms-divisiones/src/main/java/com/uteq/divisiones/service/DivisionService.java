package com.uteq.divisiones.service;

import com.uteq.divisiones.entity.Division;
import java.util.List;

public interface DivisionService {
    Division crear(Division d);
    List<Division> listar();
    Division obtener(Long id);
    Division actualizar(Long id, Division d);
    void eliminar(Long id);
}
