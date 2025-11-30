package com.uteq.admin.config;

import com.uteq.admin.entity.Rol;
import com.uteq.admin.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Inicializar roles si no existen
        if (rolRepository.count() == 0) {
            rolRepository.save(Rol.builder().nombre("ADMIN").build());
            rolRepository.save(Rol.builder().nombre("PROFESOR").build());
            rolRepository.save(Rol.builder().nombre("ALUMNO").build());
            rolRepository.save(Rol.builder().nombre("COORDINADOR").build());
            System.out.println("✅ Roles inicializados correctamente");
        }
    }
}
