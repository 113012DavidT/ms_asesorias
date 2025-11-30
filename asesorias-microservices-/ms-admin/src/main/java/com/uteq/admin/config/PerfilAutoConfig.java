package com.uteq.admin.config;

import com.uteq.admin.repository.AlumnoPerfilRepository;
import com.uteq.admin.repository.ProfesorPerfilRepository;
import com.uteq.admin.service.PerfilService;
import com.uteq.admin.service.PerfilServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PerfilAutoConfig {

    @Bean
    @Primary
    public PerfilService perfilService(ProfesorPerfilRepository profRepo, AlumnoPerfilRepository alumRepo) {
        return new PerfilServiceImpl(profRepo, alumRepo);
    }
}
