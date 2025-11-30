package com.uteq.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCrearDTO {
    private String correoMatricula;
    private String password;
    private String nombre;
    private String apellido;
    private Long rolId;  // ID del rol (ADMIN, PROFESOR, ALUMNO, COORDINADOR)
}
