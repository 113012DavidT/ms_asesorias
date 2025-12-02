package com.uteq.asesorias.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asesorias")
public class Asesoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profesorId;
    private Long alumnoId;
    private Long disponibilidadId;

    private LocalDate fecha;
    private LocalTime hora;        // compatibilidad: igual a horaInicio
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private String titulo;
    private String materia;
    private String observaciones;
    private String estatus;

    // ✅ AGREGA ESTE CAMPO NUEVO
    private LocalDate fechaRegistro;
}
