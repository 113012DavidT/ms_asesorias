package com.uteq.coordinadores.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "snapshot_programa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotPrograma {
    
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    private Long divisionId;
    
    private Boolean activo;
}
