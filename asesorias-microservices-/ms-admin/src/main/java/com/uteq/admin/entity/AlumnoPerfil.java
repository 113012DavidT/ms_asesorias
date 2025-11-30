package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "alumno_perfiles")
public class AlumnoPerfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long usuarioId;    // referencia a Usuario (ALUMNO)

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
