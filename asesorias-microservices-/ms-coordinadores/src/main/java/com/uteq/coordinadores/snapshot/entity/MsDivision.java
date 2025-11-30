package com.uteq.coordinadores.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="msdivision_coord")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsDivision {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long divisionId;

    private String clave;
    private String nombre;
    private String descripcion;

    @Column(nullable=false)
    private Long version = 1L;
}
