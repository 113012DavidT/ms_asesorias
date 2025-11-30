package com.uteq.divisiones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramaEducativoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long divisionId;
    private Boolean activo;
}
