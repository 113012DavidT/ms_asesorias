package com.uteq.asesorias.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tabla virtual/snapshot de datos de ms-admin (Profesor)
 * Se sincroniza cuando sea necesario desde ms-admin
 */
@Entity
@Table(name = "snapshot_profesor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotProfesor {
    
    @Id
    private Long id;  // mismo ID de ms-admin
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String especialidad;
    
    private Integer aniosExperiencia;
    
    private Boolean activo;
}
