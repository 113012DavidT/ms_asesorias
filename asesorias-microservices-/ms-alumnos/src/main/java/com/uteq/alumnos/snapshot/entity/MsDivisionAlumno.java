
package com.uteq.alumnos.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msdivision_alumno")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsDivisionAlumno {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long alumnoId;
  @Column(nullable=false) private Long divisionId;
  private String divisionClave; private String divisionNombre; private String divisionDescripcion;
  @Column(nullable=false) private Long version = 1L;
}
