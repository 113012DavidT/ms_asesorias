package com.uteq.admin.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaDTO {
    private Long id;
    private String clave;
    private String nombre;
    private String descripcion;
    private Long divisionId;
}
