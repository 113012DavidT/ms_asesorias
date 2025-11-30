package com.uteq.divisiones.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "divisiones")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    // 🔹 Relación con programas educativos
    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProgramaEducativo> programas;
}
