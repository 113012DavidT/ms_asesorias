package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "divisiones")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;  // Ej: "1ro A", "2do B"

    @Column(nullable = false)
    private String descripcion;

    private Boolean activo;

    private LocalDate fechaCreacion;

    private LocalDate fechaActualizacion;
}
