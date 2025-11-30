package com.uteq.coordinadores.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="msprograma_coord")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsPrograma {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long programaId;

    @Column(nullable=false)
    private Long divisionId;

    private String clave;
    private String nombre;
    private String descripcion;

    @Column(nullable=false)
    private Long version = 1L;
}
