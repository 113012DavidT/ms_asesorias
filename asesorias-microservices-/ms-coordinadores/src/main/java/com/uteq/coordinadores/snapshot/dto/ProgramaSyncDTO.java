package com.uteq.coordinadores.snapshot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramaSyncDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long divisionId;
    private Boolean activo;
}
