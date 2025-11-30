
package com.uteq.profesores.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msdivision_profesor")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsDivisionProfesor {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long profesorId;
  @Column(nullable=false) private Long divisionId;
  private String divisionClave; private String divisionNombre; private String divisionDescripcion;
  @Column(nullable=false) private Long version = 1L;
}
