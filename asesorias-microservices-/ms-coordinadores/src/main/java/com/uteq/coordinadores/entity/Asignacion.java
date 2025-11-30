package com.uteq.coordinadores.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long divisionId;

    @Column(nullable = false)
    private Long programaId;

    private String rol; // "profesor" o "alumno"

    private Boolean activo = true;
}
