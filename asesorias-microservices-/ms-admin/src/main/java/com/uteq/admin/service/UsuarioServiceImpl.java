package com.uteq.admin.service;

import com.uteq.admin.dto.UsuarioDTO;
import com.uteq.admin.dto.UsuarioCrearDTO;
import com.uteq.admin.entity.Rol;
import com.uteq.admin.entity.Usuario;
import com.uteq.admin.repository.RolRepository;
import com.uteq.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final RolRepository rolRepo;

    @Override
    public Usuario crear(Usuario u) {
        // Asignar rol desde el ID recibido
        if (u.getRol() != null && u.getRol().getId() != null) {
            Rol rol = rolRepo.findById(u.getRol().getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            u.setRol(rol);
        } else {
            throw new RuntimeException("El rol es obligatorio");
        }

        u.setActivo(true);
        u.setFechaCreacion(LocalDate.now());
        return repo.save(u);
    }

    @Override
    public UsuarioDTO crearDTO(UsuarioCrearDTO dto) {
        Rol rol = rolRepo.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = Usuario.builder()
                .correoMatricula(dto.getCorreoMatricula())
                .password(dto.getPassword())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .rol(rol)
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .build();
        
        Usuario guardado = repo.save(usuario);
        return convertirDTO(guardado);
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioCrearDTO dto) {
        Usuario usuario = obtener(id);
        
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        
        if (dto.getRolId() != null) {
            Rol rol = rolRepo.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRol(rol);
        }
        
        usuario.setFechaActualizacion(LocalDate.now());
        Usuario actualizado = repo.save(usuario);
        return convertirDTO(actualizado);
    }

    @Override
    public List<UsuarioDTO> listar() {
        return repo.findAll().stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO obtenerDTO(Long id) {
        return convertirDTO(obtener(id));
    }

    @Override
    public Usuario obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UsuarioDTO estado(Long id, Boolean activo) {
        Usuario u = obtener(id);
        u.setActivo(activo);
        u.setFechaActualizacion(LocalDate.now());
        Usuario actualizado = repo.save(u);
        return convertirDTO(actualizado);
    }

    @Override
    public Usuario login(String correo, String password) {
        Usuario u = repo.findByCorreoMatricula(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!u.getPassword().equals(password))
            throw new RuntimeException("Contraseña incorrecta");
        if (Boolean.FALSE.equals(u.getActivo()))
            throw new RuntimeException("Cuenta inactiva");
        u.setUltimoAcceso(LocalDate.now());
        return repo.save(u);
    }

    @Override
    public List<UsuarioDTO> listarPorRol(Long rolId) {
        Rol rol = rolRepo.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return repo.findByRol(rol).stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> listarActivos() {
        return repo.findByActivo(true).stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    private UsuarioDTO convertirDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .correoMatricula(usuario.getCorreoMatricula())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .activo(usuario.getActivo())
                .rolId(usuario.getRol() != null ? usuario.getRol().getId() : null)
                .rolNombre(usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN ROL")
                .build();
    }
}

