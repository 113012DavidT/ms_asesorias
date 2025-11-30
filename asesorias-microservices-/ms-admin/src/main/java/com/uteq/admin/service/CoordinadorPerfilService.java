package com.uteq.admin.service;

import com.uteq.admin.dto.CoordinadorPerfilDTO;
import com.uteq.admin.entity.CoordinadorPerfil;
import java.util.List;
import java.util.Optional;

public interface CoordinadorPerfilService {
    CoordinadorPerfil crear(CoordinadorPerfilDTO dto);
    CoordinadorPerfil actualizar(Long id, CoordinadorPerfilDTO dto);
    CoordinadorPerfil obtenerPorId(Long id);
    Optional<CoordinadorPerfil> obtenerPorUsuarioId(Long usuarioId);
    List<CoordinadorPerfil> listarTodos();
    List<CoordinadorPerfil> listarActivos();
    List<CoordinadorPerfil> listarPorDivision(Long divisionId);
    List<CoordinadorPerfil> listarPorPrograma(Long programaId);
    void eliminar(Long id);
    void desactivar(Long id);
}
