package com.uteq.admin;
import com.uteq.admin.entity.Rol;
import com.uteq.admin.entity.Usuario;
import com.uteq.admin.repository.RolRepository;
import com.uteq.admin.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDate;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class MsAdminApplication {
  private final UsuarioRepository usuarioRepository;
  private final RolRepository rolRepository;

  public static void main(String[] args){ SpringApplication.run(MsAdminApplication.class, args); }

  @PostConstruct
  public void seedAdminOnStartup() {
    String correo = "admin@uteq.edu";
    String pass = "pass123";
    usuarioRepository.findByCorreoMatricula(correo).orElseGet(() -> {
      Rol adminRol = rolRepository.findByNombre("ADMIN")
              .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ADMIN").build()));
      Usuario admin = Usuario.builder()
              .correoMatricula(correo)
              .password(pass)
              .nombre("Admin")
              .apellido("UTeQ")
              .rol(adminRol)
              .activo(true)
              .fechaCreacion(LocalDate.now())
              .build();
      Usuario creado = usuarioRepository.save(admin);
      System.out.println("✅ @PostConstruct: Usuario ADMIN creado: " + creado.getCorreoMatricula());
      return creado;
    });
  }
}
