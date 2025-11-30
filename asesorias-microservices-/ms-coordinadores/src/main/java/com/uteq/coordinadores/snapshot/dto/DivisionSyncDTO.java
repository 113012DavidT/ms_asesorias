package com.uteq.coordinadores.snapshot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DivisionSyncDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer nivel;
    private Boolean activo;
}
