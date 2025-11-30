package com.uteq.admin.config;

import com.uteq.admin.entity.Rol;
import com.uteq.admin.entity.Usuario;
import com.uteq.admin.repository.RolRepository;
import com.uteq.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        // Crear usuario ADMIN por defecto si no existe
        String defaultCorreo = "admin@uteq.edu";
        String defaultPassword = "pass123";

        usuarioRepository.findByCorreoMatricula(defaultCorreo).orElseGet(() -> {
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

            Usuario creado = usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: " + creado.getCorreoMatricula());
            return creado;
        });
    }
}
