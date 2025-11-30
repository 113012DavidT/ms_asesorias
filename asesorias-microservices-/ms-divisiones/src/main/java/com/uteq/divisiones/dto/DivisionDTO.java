package com.uteq.divisiones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DivisionDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer nivel;  // 1er año, 2do año, etc.
    private Boolean activo;
}
