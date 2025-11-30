package com.uteq.alumnos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long divisionId;

    @Column(nullable = false)
    private Long programaId;

    private String nombre;
    private String correoMatricula;
    private Boolean activo = true;
}
