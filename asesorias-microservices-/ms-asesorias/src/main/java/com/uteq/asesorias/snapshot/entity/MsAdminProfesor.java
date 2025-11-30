
package com.uteq.asesorias.snapshot.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="msadmin_profesor")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MsAdminProfesor {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long profesorUsuarioId;
  @Column(nullable=false) private Long divisionId;
  @Column(nullable=false) private Long programaId;
  @Column(nullable=false) private Long version = 1L;
}
