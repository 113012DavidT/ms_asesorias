package com.uteq.profesores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorDTO {
    private Long id;
    private Long usuarioId;
    private Long divisionId;
    private Long programaId;
    private String nombre;
    private String correoMatricula;
    private String especialidad;
    private Boolean activo;
}
