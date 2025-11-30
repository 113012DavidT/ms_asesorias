package com.uteq.coordinadores.snapshot.entity;

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
    
    private String rol;
    
    private Boolean activo;
}
