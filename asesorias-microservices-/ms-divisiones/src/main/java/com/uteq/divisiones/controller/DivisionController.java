package com.uteq.divisiones.controller;

import com.uteq.divisiones.entity.Division;
import com.uteq.divisiones.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisiones")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionService service;

    @PostMapping
    public Division crear(@RequestBody Division d) {
        return service.crear(d);
    }

    @GetMapping
    public List<Division> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Division obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public Division actualizar(@PathVariable Long id, @RequestBody Division d) {
        return service.actualizar(id, d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
