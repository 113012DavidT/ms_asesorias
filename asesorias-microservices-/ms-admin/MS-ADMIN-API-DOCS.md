## MS-ADMIN - Microservicio de Administración

### Descripción
Microservicio encargado de la administración de usuarios, roles, divisiones, programas educativos y grupos. Solo los administradores pueden acceder a este servicio.

### Estructura de Datos

#### Entidades

**Usuario**
- id: Long (PK)
- correoMatricula: String (unique, not null)
- password: String (not null)
- nombre: String (not null)
- apellido: String
- activo: Boolean (not null)
- rol: Rol (FK, not null)
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate
- ultimoAcceso: LocalDate

**Rol**
- id: Long (PK)
- nombre: String (unique, not null) - ADMIN, PROFESOR, ALUMNO, COORDINADOR

**Division**
- id: Long (PK)
- nombre: String (not null)
- descripcion: String (not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

**Programa**
- id: Long (PK)
- nombre: String (not null)
- descripcion: String (not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

**ProfesorPerfil**
- id: Long (PK)
- usuarioId: Long (unique, not null, FK de Usuario)
- division: Division (FK, not null)
- programa: Programa (FK, not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

**AlumnoPerfil**
- id: Long (PK)
- usuarioId: Long (unique, not null, FK de Usuario)
- division: Division (FK, not null)
- programa: Programa (FK, not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

**CoordinadorPerfil**
- id: Long (PK)
- usuarioId: Long (unique, not null, FK de Usuario)
- division: Division (FK, not null)
- programa: Programa (FK, not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

**Grupo**
- id: Long (PK)
- nombre: String (not null)
- descripcion: String (not null)
- profesor: ProfesorPerfil (FK, not null)
- division: Division (FK, not null)
- programa: Programa (FK, not null)
- activo: Boolean
- fechaCreacion: LocalDate
- fechaActualizacion: LocalDate

---

### API REST Endpoints

#### USUARIOS - `/api/admin/usuarios`

**Crear Usuario**
```
POST /api/admin/usuarios
Content-Type: application/json

{
  "correoMatricula": "usuario@example.com",
  "password": "password123",
  "nombre": "Juan",
  "apellido": "Pérez",
  "rolId": 1
}

Response: 201 Created
{
  "id": 1,
  "correoMatricula": "usuario@example.com",
  "nombre": "Juan",
  "apellido": "Pérez",
  "activo": true,
  "rolId": 1,
  "rolNombre": "PROFESOR"
}
```

**Listar Todos los Usuarios**
```
GET /api/admin/usuarios
Response: 200 OK
[
  {
    "id": 1,
    "correoMatricula": "usuario@example.com",
    "nombre": "Juan",
    "apellido": "Pérez",
    "activo": true,
    "rolId": 1,
    "rolNombre": "PROFESOR"
  }
]
```

**Obtener Usuario por ID**
```
GET /api/admin/usuarios/{id}
Response: 200 OK
{
  "id": 1,
  "correoMatricula": "usuario@example.com",
  "nombre": "Juan",
  "apellido": "Pérez",
  "activo": true,
  "rolId": 1,
  "rolNombre": "PROFESOR"
}
```

**Actualizar Usuario**
```
PUT /api/admin/usuarios/{id}
Content-Type: application/json
{
  "nombre": "Juan",
  "apellido": "González",
  "rolId": 1
}
Response: 200 OK
```

**Cambiar Estado (Activar/Desactivar)**
```
PUT /api/admin/usuarios/{id}/estado?activo=true
Response: 200 OK
```

**Eliminar Usuario**
```
DELETE /api/admin/usuarios/{id}
Response: 204 No Content
```

**Listar Usuarios por Rol**
```
GET /api/admin/usuarios/rol/{rolId}
Response: 200 OK
```

**Listar Usuarios Activos**
```
GET /api/admin/usuarios/activos/listar
Response: 200 OK
```

**Login**
```
POST /api/admin/usuarios/login?correo=usuario@example.com&password=password123
Response: 200 OK
{
  "id": 1,
  "correoMatricula": "usuario@example.com",
  "password": "password123",
  "nombre": "Juan",
  "apellido": "Pérez",
  "activo": true,
  "rol": { "id": 1, "nombre": "PROFESOR" },
  "fechaCreacion": "2025-01-01",
  "fechaActualizacion": null,
  "ultimoAcceso": "2025-01-15"
}
```

---

#### DIVISIONES - `/api/divisiones`

**Crear División**
```
POST /api/divisiones
Content-Type: application/json
{
  "nombre": "1ro A",
  "descripcion": "Primera paralela, turno matutino",
  "activo": true
}
Response: 201 Created
```

**Actualizar División**
```
PUT /api/divisiones/{id}
Content-Type: application/json
{
  "nombre": "1ro A",
  "descripcion": "Primera paralela, turno matutino",
  "activo": true
}
Response: 200 OK
```

**Obtener División**
```
GET /api/divisiones/{id}
Response: 200 OK
```

**Listar Todas las Divisiones**
```
GET /api/divisiones
Response: 200 OK
```

**Listar Divisiones Activas**
```
GET /api/divisiones/activos/listar
Response: 200 OK
```

**Desactivar División**
```
PUT /api/divisiones/{id}/desactivar
Response: 204 No Content
```

**Eliminar División**
```
DELETE /api/divisiones/{id}
Response: 204 No Content
```

---

#### PROGRAMAS - `/api/programas`

**Crear Programa**
```
POST /api/programas
Content-Type: application/json
{
  "nombre": "Ingeniería en Sistemas",
  "descripcion": "Carrera de 4 años en Ingeniería de Sistemas",
  "activo": true
}
Response: 201 Created
```

**Actualizar Programa**
```
PUT /api/programas/{id}
```

**Obtener Programa**
```
GET /api/programas/{id}
```

**Listar Todos los Programas**
```
GET /api/programas
```

**Listar Programas Activos**
```
GET /api/programas/activos/listar
```

**Desactivar Programa**
```
PUT /api/programas/{id}/desactivar
```

**Eliminar Programa**
```
DELETE /api/programas/{id}
```

---

#### GRUPOS - `/api/grupos`

**Crear Grupo**
```
POST /api/grupos
Content-Type: application/json
{
  "nombre": "Grupo 1",
  "descripcion": "Grupo de tutoría de matemáticas",
  "profesorId": 1,
  "divisionId": 1,
  "programaId": 1,
  "activo": true
}
Response: 201 Created
```

**Actualizar Grupo**
```
PUT /api/grupos/{id}
```

**Obtener Grupo**
```
GET /api/grupos/{id}
```

**Listar Todos los Grupos**
```
GET /api/grupos
```

**Listar Grupos Activos**
```
GET /api/grupos/activos/listar
```

**Listar Grupos por Profesor**
```
GET /api/grupos/profesor/{profesorId}
```

**Listar Grupos por División**
```
GET /api/grupos/division/{divisionId}
```

**Listar Grupos por Programa**
```
GET /api/grupos/programa/{programaId}
```

**Desactivar Grupo**
```
PUT /api/grupos/{id}/desactivar
```

**Eliminar Grupo**
```
DELETE /api/grupos/{id}
```

---

#### COORDINADORES - `/api/coordinadores`

**Crear Coordinador**
```
POST /api/coordinadores
Content-Type: application/json
{
  "usuarioId": 1,
  "divisionId": 1,
  "programaId": 1,
  "activo": true
}
Response: 201 Created
```

**Actualizar Coordinador**
```
PUT /api/coordinadores/{id}
```

**Obtener Coordinador**
```
GET /api/coordinadores/{id}
```

**Obtener Coordinador por Usuario**
```
GET /api/coordinadores/usuario/{usuarioId}
```

**Listar Todos los Coordinadores**
```
GET /api/coordinadores
```

**Listar Coordinadores Activos**
```
GET /api/coordinadores/activos/listar
```

**Listar Coordinadores por División**
```
GET /api/coordinadores/division/{divisionId}
```

**Listar Coordinadores por Programa**
```
GET /api/coordinadores/programa/{programaId}
```

**Desactivar Coordinador**
```
PUT /api/coordinadores/{id}/desactivar
```

**Eliminar Coordinador**
```
DELETE /api/coordinadores/{id}
```

---

#### PROFESORES - `/api/admin/perfiles/profesores` (Existente)

**Guardar Profesor**
```
POST /api/admin/perfiles/profesores
```

**Listar Profesores**
```
GET /api/admin/perfiles/profesores
```

**Obtener Profesor por Usuario**
```
GET /api/admin/perfiles/profesores/{usuarioId}
```

---

#### ALUMNOS - `/api/admin/perfiles/alumnos` (Existente)

**Guardar Alumno**
```
POST /api/admin/perfiles/alumnos
```

**Listar Alumnos**
```
GET /api/admin/perfiles/alumnos
```

**Obtener Alumno por Usuario**
```
GET /api/admin/perfiles/alumnos/{usuarioId}
```

---

### Flujo de Negocio

1. **ADMIN crea ROLES** (ADMIN, COORDINADOR, PROFESOR, ALUMNO)
2. **ADMIN crea DIVISIONES** (Ej: 1ro A, 2do B)
3. **ADMIN crea PROGRAMAS** (Ej: Ingeniería en Sistemas)
4. **ADMIN crea USUARIOS** con sus respectivos roles
5. **ADMIN asigna COORDINADORES** a una División y Programa
6. **COORDINADOR asigna PROFESORES** a una División y Programa
7. **COORDINADOR asigna ALUMNOS** a una División y Programa
8. **PROFESOR crea GRUPOS** asignándose a sí mismo, División y Programa
9. **PROFESOR asigna ASESORÍAS** a sus grupos

---

### Tecnologías
- **Framework**: Spring Boot 3.4.0
- **Base de Datos**: H2 (desarrollo) / PostgreSQL (producción)
- **ORM**: Hibernate/JPA
- **Lenguaje**: Java 17
- **Build**: Maven
- **Discovery**: Eureka Client (configurado pero deshabilitado por defecto)

---

### Configuración de Base de Datos

**application.yml** contiene la configuración para H2 en memoria. Para producción, cambiar:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/admin_db
    driverClassName: org.postgresql.Driver
    username: admin
    password: password
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

---

### Compilación y Ejecución

**Compilar:**
```bash
mvn clean install
```

**Ejecutar:**
```bash
mvn spring-boot:run
```

**JAR:**
```bash
java -jar target/ms-admin-1.0.0.jar
```

---

### Próximos Pasos
1. Implementar MS-Auth con JWT
2. Agregar seguridad (@EnableWebSecurity)
3. Sincronización de datos entre microservicios
4. Crear Dockerfiles para cada servicio
5. Configurar Docker-Compose para orquestación
6. Implementar logging centralizado
7. Agregar métricas y monitoreo
