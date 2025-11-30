package com.uteq.asesorias.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadDTO {
    private Long id;
    private Long profesorId;
    private String diaSemana;  // LUNES, MARTES, etc.
    private String horaInicio;  // HH:MM
    private String horaFin;    // HH:MM
    private Boolean activo;
}
