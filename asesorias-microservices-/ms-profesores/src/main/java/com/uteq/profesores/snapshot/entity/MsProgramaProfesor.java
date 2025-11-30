
package com.uteq.profesores.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msprograma_profesor")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsProgramaProfesor {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long profesorId;
  @Column(nullable=false) private Long programaId;
  private String programaClave; private String programaNombre; private String programaDescripcion;
  @Column(nullable=false) private Long version = 1L;
}
