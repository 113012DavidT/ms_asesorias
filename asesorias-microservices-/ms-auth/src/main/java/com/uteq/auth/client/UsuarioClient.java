package com.uteq.auth.client;

import com.uteq.auth.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Route through API Gateway to ensure consistent networking in Docker
@FeignClient(name = "api-gateway", url = "http://api-gateway:8000")
public interface UsuarioClient {
    
    @GetMapping("/api/admin/usuarios/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable Long id);

    @PostMapping("/api/admin/usuarios/login")
    UsuarioDTO login(@RequestParam String correo, @RequestParam String password);
}
