package com.uteq.admin.repository;

import com.uteq.admin.entity.Usuario;
import com.uteq.admin.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoMatricula(String correoMatricula);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByActivo(Boolean activo);
}

