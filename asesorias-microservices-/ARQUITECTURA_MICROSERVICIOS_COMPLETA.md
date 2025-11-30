# 🏗️ ARQUITECTURA COMPLETA - 9 MICROSERVICIOS

## 📊 Tabla de Microservicios

| # | Microservicio | Puerto | Descripción | Base Datos |
|---|---|---|---|---|
| 1 | **Eureka Server** | 8761 | Service Discovery | - |
| 2 | **MS-Auth** | 8088 | 🔐 Autenticación JWT | No aplica |
| 3 | **API Gateway** | 8000 | Enrutador + Validación | No aplica |
| 4 | **MS-Admin** | 8081 | 👨‍💼 Usuarios, Divisiones, Programas, Grupos | H2/PostgreSQL |
| 5 | **MS-Asesorías** | 8082 | 📚 Gestión de asesorías | H2/PostgreSQL |
| 6 | **MS-Coordinadores** | 8083 | 👥 Coordinación de académicos | H2/PostgreSQL |
| 7 | **MS-Divisiones** | 8084 | 🏫 Gestión de divisiones | H2/PostgreSQL |
| 8 | **MS-Profesores** | 8085 | 📖 Perfiles y disponibilidad | H2/PostgreSQL |
| 9 | **MS-Alumnos** | 8086 | 🎓 Perfiles de estudiantes | H2/PostgreSQL |

## 🔄 Flujo de Solicitud

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ HTTP Request
       ▼
┌──────────────────────────────┐
│   API GATEWAY (8000)         │
├──────────────────────────────┤
│ - Enrutamiento               │
│ - Validación JWT (ms-auth)   │
│ - Agregación Headers         │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  EUREKA (8761)               │
│ - Descubre microservicio     │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ Microservicio Destino        │
│ (ms-admin, ms-asesorias, etc)│
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ Base de Datos (H2 o PG)      │
└──────────────────────────────┘
```

## 📋 MS-AUTH (Puerto 8088) - Autenticación y Autorización

### Responsabilidades
- ✅ Generar tokens JWT
- ✅ Validar tokens
- ✅ Refrescar tokens
- ✅ Agregar claims (rol, usuario, etc)

### Endpoints
```
POST   /api/auth/login         - Generar token
POST   /api/auth/validate      - Validar token
POST   /api/auth/refresh       - Refrescar token
GET    /api/auth/health        - Estado del servicio
```

### Estructura JWT
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}

Payload: {
  "sub": "1",
  "correoMatricula": "usuario@uteq.edu",
  "rol": "ADMIN",
  "exp": 1705420800
}
```

**Documentación**: Ver `MS-AUTH-DOCUMENTACION.md`

---

## 🔐 API GATEWAY (Puerto 8000)

### Responsabilidades
- ✅ Enrutamiento de solicitudes
- ✅ Validación de tokens JWT
- ✅ Agregación de headers
- ✅ Rate limiting (futuro)
- ✅ Cors configuration

### Rutas Públicas
```
POST /api/auth/login
GET  /api/auth/health
```

### Rutas Protegidas (Requieren Token)
```
GET    /api/divisiones
POST   /api/divisiones
PUT    /api/divisiones/{id}
DELETE /api/divisiones/{id}
... y más
```

---

## 👨‍💼 MS-ADMIN (Puerto 8081) - Administración Central

### Responsabilidades
- ✅ Gestión de usuarios
- ✅ Gestión de roles
- ✅ Gestión de divisiones
- ✅ Gestión de programas educativos
- ✅ Gestión de grupos
- ✅ Asignación de coordinadores

### Entidades
```
Usuario → Rol
Usuario → ProfesorPerfil/AlumnoPerfil/CoordinadorPerfil
Division
Programa
Grupo (asignado a Profesor, Division, Programa)
CoordinadorPerfil (asignado a Division, Programa)
```

### Endpoints Principales
```
POST   /api/admin/usuarios             - Crear usuario
GET    /api/admin/usuarios             - Listar usuarios
PUT    /api/admin/usuarios/{id}        - Actualizar usuario
DELETE /api/admin/usuarios/{id}        - Eliminar usuario
POST   /api/admin/usuarios/login       - Login

POST   /api/divisiones                 - Crear división
GET    /api/divisiones                 - Listar divisiones
PUT    /api/divisiones/{id}            - Actualizar

POST   /api/programas                  - Crear programa
GET    /api/programas                  - Listar programas

POST   /api/grupos                     - Crear grupo
GET    /api/grupos/profesor/{id}       - Grupos por profesor

POST   /api/coordinadores              - Crear coordinador
GET    /api/coordinadores/division/{id} - Por división
```

### Base de Datos
```
usuarios
├── id (PK)
├── correoMatricula (unique)
├── nombre
├── apellido
├── rol_id (FK)
└── activo

roles
├── id (PK)
└── nombre (ADMIN, PROFESOR, ALUMNO, COORDINADOR)

divisiones
├── id (PK)
├── nombre
├── descripcion
└── activo

programas
├── id (PK)
├── nombre
├── descripcion
└── activo

profesor_perfiles
├── id (PK)
├── usuario_id (FK)
├── division_id (FK)
├── programa_id (FK)
└── activo

alumno_perfiles
├── id (PK)
├── usuario_id (FK)
├── division_id (FK)
├── programa_id (FK)
└── activo

coordinador_perfiles
├── id (PK)
├── usuario_id (FK)
├── division_id (FK)
├── programa_id (FK)
└── activo

grupos
├── id (PK)
├── nombre
├── profesor_id (FK)
├── division_id (FK)
├── programa_id (FK)
└── activo
```

**Documentación**: Ver `ms-admin/MS-ADMIN-API-DOCS.md`

---

## 📚 MS-ASESORÍAS (Puerto 8082) - Gestión de Asesorías

### Responsabilidades
- Crear solicitudes de asesorías
- Gestionar disponibilidad de profesores
- Asignar asesorías a grupos
- Generar reportes
- Notificar cambios

### Entidades (Modelo Propuesto)
```
Asesoria
├── id
├── grupo_id (FK a ms-admin)
├── profesor_id (FK a ms-admin)
├── alumno_id (FK a ms-admin)
├── fecha
├── hora_inicio
├── hora_fin
├── titulo
├── descripcion
├── estado (PENDIENTE, CONFIRMADA, CANCELADA)
└── timestamp

SolicitudAsesoria
├── id
├── alumno_id (FK)
├── profesor_id (FK)
├── fecha_solicitada
├── tema
├── estado (PENDIENTE, ACEPTADA, RECHAZADA)
└── timestamp

DisponibilidadProfesor
├── id
├── profesor_id (FK)
├── dia_semana
├── hora_inicio
├── hora_fin
└── activo
```

### Endpoints (Propuesto)
```
POST   /api/asesorias                   - Crear asesoría
GET    /api/asesorias                   - Listar
GET    /api/asesorias/profesor/{id}     - Por profesor
PUT    /api/asesorias/{id}              - Actualizar
DELETE /api/asesorias/{id}              - Cancelar

POST   /api/solicitudes                 - Crear solicitud
GET    /api/solicitudes/{alumnoId}      - Mis solicitudes
PUT    /api/solicitudes/{id}/aceptar    - Aceptar
PUT    /api/solicitudes/{id}/rechazar   - Rechazar

POST   /api/disponibilidad              - Establecer disponibilidad
GET    /api/disponibilidad/{profesorId} - Ver disponibilidad
```

---

## 👥 MS-COORDINADORES (Puerto 8083) - Coordinación

### Responsabilidades
- Gestionar coordinadores
- Aprobar asignaciones de profesores
- Generar reportes de coordinación
- Gestionar horarios académicos

### Endpoints (Propuesto)
```
POST   /api/coordinadores               - Crear coordinador
GET    /api/coordinadores               - Listar
GET    /api/coordinadores/{id}          - Por ID
PUT    /api/coordinadores/{id}          - Actualizar
DELETE /api/coordinadores/{id}          - Eliminar

POST   /api/asignaciones                - Asignar profesor/alumno
GET    /api/asignaciones/division/{id}  - Por división
PUT    /api/asignaciones/{id}           - Actualizar
```

---

## 🏫 MS-DIVISIONES (Puerto 8084) - Gestión de Divisiones

### Responsabilidades
- Gestionar divisiones/grados
- Horarios de divisiones
- Aulas asignadas
- Información de divisiones

### Endpoints (Propuesto)
```
POST   /api/divisiones                  - Crear (ya en ms-admin)
GET    /api/divisiones                  - Listar
GET    /api/divisiones/{id}             - Detalles
PUT    /api/divisiones/{id}             - Actualizar
DELETE /api/divisiones/{id}             - Eliminar

POST   /api/horarios                    - Establecer horario
GET    /api/horarios/division/{id}      - Ver horario
```

---

## 📖 MS-PROFESORES (Puerto 8085) - Gestión de Profesores

### Responsabilidades
- Gestionar perfiles de profesores
- Especialidades
- Disponibilidad
- Calificaciones

### Endpoints (Propuesto)
```
POST   /api/profesores                  - Crear profesor
GET    /api/profesores                  - Listar
GET    /api/profesores/{id}             - Detalles
PUT    /api/profesores/{id}             - Actualizar
DELETE /api/profesores/{id}             - Eliminar

GET    /api/profesores/{id}/disponibilidad
POST   /api/profesores/{id}/especialidades
GET    /api/profesores/{id}/calificaciones
```

---

## 🎓 MS-ALUMNOS (Puerto 8086) - Gestión de Alumnos

### Responsabilidades
- Gestionar perfiles de alumnos
- Historial académico
- Solicitudes de asesorías
- Progreso académico

### Endpoints (Propuesto)
```
POST   /api/alumnos                     - Crear alumno
GET    /api/alumnos                     - Listar
GET    /api/alumnos/{id}                - Detalles
PUT    /api/alumnos/{id}                - Actualizar
DELETE /api/alumnos/{id}                - Eliminar

GET    /api/alumnos/{id}/historial
GET    /api/alumnos/{id}/asesorias
GET    /api/alumnos/{id}/calificaciones
```

---

## 📦 Dependencias Compartidas

Todos los microservicios usan:

```xml
<!-- Spring Boot -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Eureka Client -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- OpenFeign (para comunicación inter-servicios) -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- JPA + Hibernate -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
</dependency>

<!-- H2 Database (desarrollo) -->
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```

---

## 🐳 Estructura Docker

### Archivo: docker-compose.yml

```yaml
services:
  eureka-server        # 8761 - Service Discovery
  ms-auth              # 8088 - Autenticación
  api-gateway          # 8000 - Enrutador
  ms-admin             # 8081 - Administración
  ms-asesorias         # 8082 - Asesorías
  ms-coordinadores     # 8083 - Coordinación
  ms-divisiones        # 8084 - Divisiones
  ms-profesores        # 8085 - Profesores
  ms-alumnos           # 8086 - Alumnos
```

### Comandos Útiles

```bash
# Construir todas las imágenes
docker-compose build

# Iniciar todos los servicios
docker-compose up -d

# Ver logs de un servicio
docker-compose logs -f ms-admin

# Detener todos los servicios
docker-compose down

# Detener y limpiar volúmenes
docker-compose down -v
```

---

## 🔗 Integración Inter-Servicios

### Comunicación con OpenFeign

**Ejemplo: MS-Asesorías llamando a MS-Admin**

```java
@FeignClient(name = "ms-admin", url = "http://ms-admin:8081")
public interface UsuarioClient {
    @GetMapping("/api/admin/usuarios/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable Long id);
}
```

### Patrones de Sincronización

1. **Síncrono (REST + OpenFeign)**
   - Útil para datos críticos
   - Ms-Asesorías llama a Ms-Admin

2. **Asíncrono (Events)**
   - RabbitMQ o Kafka (futuro)
   - Notificaciones entre servicios

3. **Caché Local (Sync Service)**
   - Copias locales de datos
   - Reducir llamadas inter-servicios

---

## 🚀 Flujo de Inicio

### 1. Levantando Servicios
```
1. Eureka Server (8761)      - Esperar health check
2. MS-Auth (8088)             - Esperar health check
3. API Gateway (8000)         - Esperar health check
4. MS-Admin (8081)            - Inicializar BD, cargar datos
5. Resto de MS (8082-8086)   - Conectarse a Eureka
```

### 2. Verificar Estado
```bash
# Eureka Dashboard
http://localhost:8761

# Verificar registros
curl http://localhost:8761/eureka/apps
```

### 3. Realizar Login
```bash
curl -X POST "http://localhost:8000/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "admin@uteq.edu",
    "password": "admin123"
  }'
```

---

## 📊 Diagrama de Dependencias

```
┌─────────────────────────────────────────────────┐
│                 CLIENTE                         │
└────────────┬──────────────────────────────────┘
             │
             ▼
         ┌────────────┐
         │ API GATEWAY│─────┐
         └────────────┘     │
             │              │
             │              ▼
             │         ┌─────────┐
             │         │ MS-Auth │
             │         └─────────┘
             │
      ┌──────┼───────────────┬────────────────┐
      │      │               │                │
      ▼      ▼               ▼                ▼
   ┌─────┐┌─────────┐┌────────────┐┌──────────────┐
   │Admin││Asesorias││Coordinad.  ││Divisiones   │
   └─────┘└─────────┘└────────────┘└──────────────┘
             │
      ┌──────┼──────────────┐
      │      │              │
      ▼      ▼              ▼
   ┌─────────────┐  ┌─────────────┐
   │ Profesores  │  │  Alumnos    │
   └─────────────┘  └─────────────┘

   ┌──────────────────────────────┐
   │  EUREKA (Service Discovery)  │
   └──────────────────────────────┘
```

---

## ✅ Checklist de Implementación

- [x] Eureka Server
- [x] MS-Auth con JWT
- [x] API Gateway con validación
- [x] MS-Admin (completo)
- [ ] MS-Asesorías (estructura base)
- [ ] MS-Coordinadores (estructura base)
- [ ] MS-Divisiones (estructura base)
- [ ] MS-Profesores (estructura base)
- [ ] MS-Alumnos (estructura base)
- [ ] Sincronización de datos
- [ ] Tests unitarios
- [ ] Tests integración
- [ ] CI/CD pipeline
- [ ] Kubernetes deployment

---

**Versión**: 1.0.0
**Última actualización**: Enero 2025
**Estado**: ✅ Arquitectura Diseñada, MS-Auth Implementado
