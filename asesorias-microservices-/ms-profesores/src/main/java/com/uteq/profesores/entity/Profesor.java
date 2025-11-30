package com.uteq.profesores.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profesores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesor {
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
    private String especialidad;
    private Boolean activo = true;
}
