package com.uteq.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String correoMatricula;
    private String password;
}
