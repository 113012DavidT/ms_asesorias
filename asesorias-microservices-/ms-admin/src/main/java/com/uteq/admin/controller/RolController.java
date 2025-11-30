package com.uteq.admin.controller;

import com.uteq.admin.entity.Rol;
import com.uteq.admin.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository repo;

    // 🔹 Listar todos los roles
    @GetMapping
    public List<Rol> listar() {
        return repo.findAll();
    }

    // 🔹 Crear un rol manualmente (si se necesita)
    @PostMapping
    public Rol crear(@RequestBody Rol rol) {
        return repo.save(rol);
    }

    // 🔹 Buscar rol por ID
    @GetMapping("/{id}")
    public Rol obtener(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
}
