package com.uteq.coordinadores.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "snapshot_division")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotDivision {
    
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    private Integer nivel;
    
    private Boolean activo;
}
