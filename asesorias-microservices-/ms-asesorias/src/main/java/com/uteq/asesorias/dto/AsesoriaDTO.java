package com.uteq.asesorias.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsesoriaDTO {
    private Long id;
    private Long profesorId;
    private Long alumnoId;
    private Long disponibilidadId;
    private String materia;
    private String observaciones;
    private String estatus;  // PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
    private String fecha;
    private String hora;
}
