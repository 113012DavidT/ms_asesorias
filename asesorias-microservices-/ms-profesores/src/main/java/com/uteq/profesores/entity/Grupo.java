package com.uteq.profesores.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "grupos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long profesorUsuarioId;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();
}
