package com.uteq.asesorias.snapshot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoSyncDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String matricula;
    private Long divisionId;
    private Long programaId;
    private Double promedio;
    private Boolean activo;
}
