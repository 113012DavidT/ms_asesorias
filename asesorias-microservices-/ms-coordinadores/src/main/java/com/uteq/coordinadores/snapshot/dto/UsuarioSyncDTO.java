package com.uteq.coordinadores.snapshot.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSyncDTO {
    private Long id;
    private String nombre;
    private String correoMatricula;
    private String rol;
    private Boolean activo;
}
