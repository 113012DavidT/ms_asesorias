package com.uteq.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationResponse {
    private Boolean valid;
    private Long usuarioId;
    private String correoMatricula;
    private String rolNombre;
    private String mensaje;
}
