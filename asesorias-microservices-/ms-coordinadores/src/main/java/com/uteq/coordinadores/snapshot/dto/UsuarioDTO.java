package com.uteq.coordinadores.snapshot.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String correoMatricula;
    private String nombre;
    private Boolean activo;
}
