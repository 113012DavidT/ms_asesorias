package com.uteq.admin.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String correoMatricula;
    private String nombre;
    private String apellido;
    private String rol;  // nombre del rol (no objeto)
    private String rolNombre;
    private Long rolId;
    private Boolean activo;
}
