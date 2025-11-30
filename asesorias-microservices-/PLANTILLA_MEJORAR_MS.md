# 📚 PLANTILLA PARA MEJORAR OTROS MICROSERVICIOS

## 🎯 Cómo Aplicar el Patrón a Otros MS

Este documento te muestra paso a paso cómo mejorar los otros microservicios siguiendo el patrón implementado en MS-Admin y MS-Asesorias.

---

## PASO 1: Revisar Entidades Existentes

### Checklist
```java
// ✅ Verifica que cada entidad tenga:
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tabla_name")
public class MiEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Campos con validaciones
    @Column(nullable = false)
    private String nombre;
    
    // Relaciones apropiadas
    @ManyToOne
    @JoinColumn(name = "fk_id")
    private OtraEntidad relacion;
    
    // Campos de auditoría
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
}
```

---

## PASO 2: Mejorar Repositorios

### Template de Repository
```java
package com.uteq.miservicio.repository;

import com.uteq.miservicio.entity.MiEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MiEntidadRepository extends JpaRepository<MiEntidad, Long> {
    // Búsquedas básicas
    Optional<MiEntidad> findByNombre(String nombre);
    List<MiEntidad> findByActivo(Boolean activo);
    
    // Búsquedas por FK
    List<MiEntidad> findByOtraEntidadId(Long otraEntidadId);
    
    // Búsquedas personalizadas (agregar según necesidad)
    // List<MiEntidad> findByCampoAndOtroCampo(String campo, String otroCampo);
}
```

### Ejemplo Real: MS-Coordinadores
```java
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByCoordinadorId(Long coordinadorId);
    List<Asignacion> findByDivisionId(Long divisionId);
    List<Asignacion> findByProgramaId(Long programaId);
    Optional<Asignacion> findByCoordinadorIdAndDivisionIdAndProgramaId(
        Long coordinadorId, Long divisionId, Long programaId);
}
```

---

## PASO 3: Expandir Servicios

### Template de Service Interface
```java
package com.uteq.miservicio.service;

import com.uteq.miservicio.dto.MiEntidadDTO;
import com.uteq.miservicio.entity.MiEntidad;
import java.util.List;
import java.util.Optional;

public interface MiEntidadService {
    // CREAR
    MiEntidad crear(MiEntidadDTO dto);
    
    // LEER
    Optional<MiEntidad> obtenerPorId(Long id);
    List<MiEntidad> listarTodos();
    List<MiEntidad> listarActivos();
    
    // ACTUALIZAR
    MiEntidad actualizar(Long id, MiEntidadDTO dto);
    
    // ELIMINAR
    void eliminar(Long id);
    void desactivar(Long id);
    
    // BÚSQUEDAS PERSONALIZADAS
    List<MiEntidad> listarPorCriterio(String criterio);
}
```

### Template de Service Implementation
```java
package com.uteq.miservicio.service.impl;

import com.uteq.miservicio.dto.MiEntidadDTO;
import com.uteq.miservicio.entity.MiEntidad;
import com.uteq.miservicio.repository.MiEntidadRepository;
import com.uteq.miservicio.service.MiEntidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MiEntidadServiceImpl implements MiEntidadService {
    
    private final MiEntidadRepository repository;
    
    @Override
    public MiEntidad crear(MiEntidadDTO dto) {
        MiEntidad entidad = MiEntidad.builder()
            .nombre(dto.getNombre())
            .descripcion(dto.getDescripcion())
            .activo(true)
            .fechaCreacion(LocalDate.now())
            .build();
        return repository.save(entidad);
    }
    
    @Override
    public Optional<MiEntidad> obtenerPorId(Long id) {
        return repository.findById(id);
    }
    
    @Override
    public List<MiEntidad> listarTodos() {
        return repository.findAll();
    }
    
    @Override
    public List<MiEntidad> listarActivos() {
        return repository.findByActivo(true);
    }
    
    @Override
    public MiEntidad actualizar(Long id, MiEntidadDTO dto) {
        MiEntidad entidad = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No encontrado"));
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setFechaActualizacion(LocalDate.now());
        return repository.save(entidad);
    }
    
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public void desactivar(Long id) {
        MiEntidad entidad = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No encontrado"));
        entidad.setActivo(false);
        entidad.setFechaActualizacion(LocalDate.now());
        repository.save(entidad);
    }
    
    @Override
    public List<MiEntidad> listarPorCriterio(String criterio) {
        return repository.findByNombre(criterio);
    }
}
```

---

## PASO 4: Crear/Mejorar Controladores

### Template de Controller
```java
package com.uteq.miservicio.controller;

import com.uteq.miservicio.dto.MiEntidadDTO;
import com.uteq.miservicio.entity.MiEntidad;
import com.uteq.miservicio.service.MiEntidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mientidades")
@RequiredArgsConstructor
public class MiEntidadController {
    
    private final MiEntidadService service;
    
    @PostMapping
    public ResponseEntity<MiEntidad> crear(@RequestBody MiEntidadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }
    
    @GetMapping
    public ResponseEntity<List<MiEntidad>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Optional<MiEntidad>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MiEntidad> actualizar(@PathVariable Long id, @RequestBody MiEntidadDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/activos/listar")
    public ResponseEntity<List<MiEntidad>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }
}
```

---

## PASO 5: Crear DTOs

### Template de DTO
```java
package com.uteq.miservicio.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiEntidadDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Long relacion Id;  // Si tiene FK
}
```

---

## PASO 6: Mejorar Configuración

### application.yml mejorado
```yaml
server:
  port: 8083  # Ajusta según el puerto del MS

spring:
  application:
    name: ms-miservicio

  datasource:
    url: jdbc:h2:mem:miservicio_db
    driverClassName: org.h2.Driver
    username: sa
    password:

  sql:
    init:
      mode: always
      data-locations: classpath:data.sql

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate:
        '[format_sql]': true

  h2:
    console:
      enabled: true
      path: /h2-console

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    enabled: false
```

---

## PASO 7: Crear Datos de Prueba

### data.sql template
```sql
-- Insertar datos de ejemplo
INSERT INTO mi_entidades (nombre, descripcion, activo, fecha_creacion) 
VALUES ('Nombre 1', 'Descripción 1', true, CURRENT_DATE);

INSERT INTO mi_entidades (nombre, descripcion, activo, fecha_creacion) 
VALUES ('Nombre 2', 'Descripción 2', true, CURRENT_DATE);
```

---

## 📋 Checklist por Microservicio

### MS-Coordinadores (⏳ PRÓXIMO)
```
Entidades:
- [ ] Asignacion (coordinadorId, divisionId, programaId)

Repositorios:
- [ ] AsignacionRepository (con búsquedas por coordinador, división, programa)

Servicios:
- [ ] AsignacionService/Impl (CRUD completo)

Controladores:
- [ ] AsignacionController (7+ endpoints)

Extras:
- [ ] data.sql (datos de prueba)
- [ ] Dockerfile
- [ ] application.yml
```

### MS-Divisiones (⏳ DESPUÉS)
```
Entidades:
- [ ] Division (mejorado con campos adicionales)
- [ ] ProgramaEducativo (mejorado)

Repositorios:
- [ ] DivisionRepository (expandido)
- [ ] ProgramaEducativoRepository (expandido)

Servicios:
- [ ] DivisionService/Impl (CRUD completo)
- [ ] ProgramaEducativoService/Impl (CRUD completo)

Controladores:
- [ ] DivisionController (7+ endpoints)
- [ ] ProgramaEducativoController (7+ endpoints)

Extras:
- [ ] data.sql
- [ ] Dockerfile
- [ ] application.yml
```

### MS-Profesores (⏳ DESPUÉS)
```
Similar a MS-Coordinadores pero para Profesor

Sincronización:
- [ ] AdminClient (llamadas a MS-Admin)
- [ ] AsesoriasClient (llamadas a MS-Asesorias)

Endpoints especiales:
- [ ] GET /api/profesores/{id}/perfil (usuario + profesor)
- [ ] GET /api/profesores/{id}/asesorias (desde MS-Asesorias)
- [ ] POST /api/profesores/{id}/disponibilidades (crear en MS-Asesorias)
```

### MS-Alumnos (⏳ DESPUÉS)
```
Similar a MS-Profesores pero para Alumno

Sincronización:
- [ ] AdminClient
- [ ] AsesoriasClient

Endpoints especiales:
- [ ] GET /api/alumnos/{id}/perfil (usuario + alumno)
- [ ] GET /api/alumnos/{id}/asesorias (desde MS-Asesorias)
- [ ] POST /api/alumnos/{id}/solicitar-asesoria (crear en MS-Asesorias)
```

---

## 🚀 Comandos Útiles

```bash
# Compilar un MS específico
cd ms-coordinadores
mvn clean install

# Ejecutar localmente
mvn spring-boot:run

# Ejecutar docker-compose para probar
docker-compose build ms-coordinadores
docker-compose up ms-coordinadores

# Ver logs
docker-compose logs -f ms-coordinadores

# Ejecutar tests
mvn test

# Limpiar e instalar
mvn clean install -DskipTests
```

---

## 📞 Contacto con Otros MS

### Cliente HTTP (OpenFeign)

```java
@FeignClient(name = "ms-admin")
public interface AdminClient {
    @GetMapping("/api/admin/usuarios/{id}")
    Usuario obtenerUsuario(@PathVariable Long id);
    
    @GetMapping("/api/divisiones/{id}")
    Division obtenerDivision(@PathVariable Long id);
}

@FeignClient(name = "ms-asesorias")
public interface AsesoriasClient {
    @GetMapping("/api/asesorias/profesor/{id}")
    List<Asesoria> obtenerAsesoriasProfesor(@PathVariable Long id);
}
```

---

**Última actualización**: 2025-01-15
**Versión**: 1.0.0
