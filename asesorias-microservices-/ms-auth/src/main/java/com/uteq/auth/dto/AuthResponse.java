package com.uteq.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type;
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String rolNombre;
    private String correoMatricula;
    private Long expiresIn;

    public AuthResponse(String token, String refreshToken, Long usuarioId, String nombre, String apellido, String rolNombre, String correoMatricula) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.type = "Bearer";
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rolNombre = rolNombre;
        this.correoMatricula = correoMatricula;
        this.expiresIn = 86400L; // 24 horas en segundos
    }
}
