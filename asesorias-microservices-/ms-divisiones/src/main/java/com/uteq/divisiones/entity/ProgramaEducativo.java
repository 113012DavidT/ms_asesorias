package com.uteq.divisiones.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "programas_educativos")
public class ProgramaEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    // 🔹 Relación con la división (LAZY para evitar recursión)
    // @JsonIgnore en serialización pero permite deserialización
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)
    @JsonIgnore(value = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Division division;
}
