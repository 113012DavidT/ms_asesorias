package com.uteq.divisiones.controller;

import com.uteq.divisiones.entity.ProgramaEducativo;
import com.uteq.divisiones.service.ProgramaService;
import com.uteq.divisiones.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/programas")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;
    private final DivisionService divisionService;

    @PostMapping
    public ProgramaEducativo crear(@RequestBody ProgramaEducativo p) {
        // La división debe venir en el objeto con al menos el ID
        if (p.getDivision() == null || (p.getDivision().getId() == null)) {
            throw new RuntimeException("Debe especificar una división válida");
        }
        // Obtener la división completa desde la BD usando el ID
        var division = divisionService.obtener(p.getDivision().getId());
        p.setDivision(division);
        return programaService.crear(p);
    }

    @GetMapping
    public List<ProgramaEducativo> listar() {
        return programaService.listar();
    }

    @GetMapping("/{id}")
    public ProgramaEducativo obtener(@PathVariable Long id) {
        return programaService.obtener(id);
    }

    @PutMapping("/{id}")
    public ProgramaEducativo actualizar(@PathVariable Long id, @RequestBody ProgramaEducativo p) {
        return programaService.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
    }

    @GetMapping("/division/{divisionId}")
    public List<ProgramaEducativo> listarPorDivision(@PathVariable Long divisionId) {
        return programaService.listarPorDivision(divisionId);
    }
}
