package com.uteq.coordinadores.snapshot.dto;

import lombok.Data;

@Data
public class ProgramaDTO {
    private Long id;
    private Long divisionId;
    private String clave;
    private String nombre;
    private String descripcion;
}
