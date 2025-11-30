package com.uteq.admin.config;

import com.uteq.admin.entity.Rol;
import com.uteq.admin.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepo;

    @Override
    public void run(String... args) {
        // Lista predefinida de roles
        List<String> rolesPredefinidos = List.of("ADMIN", "PROFESOR", "ALUMNO", "COORDINADOR");

        // Insertar solo si no existen
        for (String nombre : rolesPredefinidos) {
            rolRepo.findByNombre(nombre)
                    .orElseGet(() -> {
                        Rol nuevoRol = Rol.builder().nombre(nombre).build();
                        rolRepo.save(nuevoRol);
                        System.out.println("✅ Rol creado: " + nombre);
                        return nuevoRol;
                    });
        }
    }
}
