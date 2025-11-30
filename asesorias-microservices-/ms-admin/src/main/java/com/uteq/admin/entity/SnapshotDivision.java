package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "snapshot_divisiones")
public class SnapshotDivision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "divisiones_id")
    private Long divisionesId;  // ID original de ms-divisiones

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
}
