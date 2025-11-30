package com.uteq.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String correoMatricula;
    private String nombre;
    private String apellido;
    private Boolean activo;
    private Long rolId;
    private String rolNombre;
}
