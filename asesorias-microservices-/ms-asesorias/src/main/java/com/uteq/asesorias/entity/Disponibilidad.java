package com.uteq.asesorias.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "disponibilidades")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profesorId;         // ID del profesor (usuario)
    private LocalDate fecha;         // ✅ Tipo LocalDate en lugar de String
    private LocalTime horaInicio;    // ✅ Tipo LocalTime en lugar de String
    private LocalTime horaFin;       // ✅ Tipo LocalTime en lugar de String

    private Boolean disponible;

    @PrePersist
    public void prePersist() {
        if (disponible == null) disponible = true;
    }
}
