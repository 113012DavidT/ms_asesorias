package com.uteq.asesorias.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tabla virtual/snapshot de datos de ms-admin (Alumno)
 * Se sincroniza cuando sea necesario desde ms-admin
 */
@Entity
@Table(name = "snapshot_alumno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotAlumno {
    
    @Id
    private Long id;  // mismo ID de ms-admin
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String matricula;
    
    private Long divisionId;
    
    private Long programaId;
    
    private Double promedio;
    
    private Boolean activo;
}
