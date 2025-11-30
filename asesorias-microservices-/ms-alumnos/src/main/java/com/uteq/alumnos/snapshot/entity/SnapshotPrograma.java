package com.uteq.alumnos.snapshot.entity;

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
    
    private Long divisionId;
    
    private Boolean activo;
}
