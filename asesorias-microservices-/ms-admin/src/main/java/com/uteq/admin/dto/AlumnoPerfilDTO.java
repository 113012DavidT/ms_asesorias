package com.uteq.admin.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoPerfilDTO {
    private Long id;
    private Long usuarioId;
    private Long divisionId;
    private Long programaId;
    private Boolean activo;
}
