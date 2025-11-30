package com.uteq.coordinadores.snapshot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="msadmin_profesor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsAdminProfesor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long profesorId;

    private String correoMatricula;
    private String nombre;
    private Boolean activo;

    @Column(nullable=false)
    private Long version = 1L;
}
