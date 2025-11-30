package com.uteq.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationRequest {
    private String token;
}
