package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "snapshot_programas")
public class SnapshotPrograma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "programas_id")
    private Long programasId;  // ID original de ms-divisiones

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, name = "division_id")
    private Long divisionId;  // ID del snapshot de división
}
