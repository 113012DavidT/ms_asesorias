package com.uteq.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long profesorId;
    private Long divisionId;
    private Long programaId;
    private Boolean activo;
}
