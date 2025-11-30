package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "grupos")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;  // Ej: "Grupo 1", "Grupo A"

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private ProfesorPerfil profesor;

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
