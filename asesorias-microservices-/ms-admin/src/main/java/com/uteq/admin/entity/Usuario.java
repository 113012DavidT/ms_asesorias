package com.uteq.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String correoMatricula;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    private String apellido;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    private LocalDate fechaCreacion;

    private LocalDate fechaActualizacion;

    private LocalDate ultimoAcceso;
}
