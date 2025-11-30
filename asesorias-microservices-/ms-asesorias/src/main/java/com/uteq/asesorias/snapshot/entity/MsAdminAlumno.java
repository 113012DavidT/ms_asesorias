
package com.uteq.asesorias.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msadmin_alumno")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsAdminAlumno {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long alumnoUsuarioId;
  @Column(nullable=false) private Long divisionId;
  @Column(nullable=false) private Long programaId;
  @Column(nullable=false) private Long version = 1L;
}
