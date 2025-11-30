package com.uteq.asesorias.snapshot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorSyncDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String especialidad;
    private Integer aniosExperiencia;
    private Boolean activo;
}
