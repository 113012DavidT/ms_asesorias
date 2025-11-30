package com.uteq.admin.service;

import com.uteq.admin.dto.UsuarioDTO;
import com.uteq.admin.dto.UsuarioCrearDTO;
import com.uteq.admin.entity.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario crear(Usuario u);
    UsuarioDTO crearDTO(UsuarioCrearDTO dto);
    UsuarioDTO actualizar(Long id, UsuarioCrearDTO dto);
    List<UsuarioDTO> listar();
    UsuarioDTO obtenerDTO(Long id);
    Usuario obtener(Long id);
    UsuarioDTO estado(Long id, Boolean activo);
    Usuario login(String correo, String password);
    List<UsuarioDTO> listarPorRol(Long rolId);
    List<UsuarioDTO> listarActivos();
    void eliminar(Long id);
}

