package com.uteq.admin.config;

import com.uteq.admin.entity.Rol;
import com.uteq.admin.entity.Usuario;
import com.uteq.admin.repository.RolRepository;
import com.uteq.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminUserLoader implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String defaultCorreo = "admin@uteq.edu";
        String defaultPassword = "pass123";

        if (usuarioRepository.findByCorreoMatricula(defaultCorreo).isEmpty()) {
            Rol adminRol = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ADMIN").build()));

            Usuario admin = Usuario.builder()
                    .correoMatricula(defaultCorreo)
                    .password(defaultPassword)
                    .nombre("Admin")
                    .apellido("UTeQ")
                    .rol(adminRol)
                    .activo(true)
                    .fechaCreacion(LocalDate.now())
                    .build();

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN inicial sembrado: " + defaultCorreo);
        } else {
            System.out.println("ℹ️ Usuario ADMIN ya existe: " + defaultCorreo);
        }
    }
}
