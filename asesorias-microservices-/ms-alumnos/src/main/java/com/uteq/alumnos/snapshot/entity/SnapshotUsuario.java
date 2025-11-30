package com.uteq.alumnos.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "snapshot_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotUsuario {
    
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String correoMatricula;
    
    private Boolean activo;
}
