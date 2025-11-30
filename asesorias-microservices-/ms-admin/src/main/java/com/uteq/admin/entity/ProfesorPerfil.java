package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "profesor_perfiles")
public class ProfesorPerfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long usuarioId;    // referencia a Usuario (PROFESOR)

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    @ManyToOne
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    private Boolean activo;

    private LocalDate fechaCreacion;

    private LocalDate fechaActualizacion;
}
