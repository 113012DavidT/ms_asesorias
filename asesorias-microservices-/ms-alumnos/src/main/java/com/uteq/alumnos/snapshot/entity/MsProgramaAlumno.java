
package com.uteq.alumnos.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msprograma_alumno")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsProgramaAlumno {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long alumnoId;
  @Column(nullable=false) private Long programaId;
  private String programaClave; private String programaNombre; private String programaDescripcion;
  @Column(nullable=false) private Long version = 1L;
}
