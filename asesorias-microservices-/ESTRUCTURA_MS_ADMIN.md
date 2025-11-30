# RESUMEN DE LA ESTRUCTURA MS-ADMIN

## 📊 Diagrama de Relaciones

```
┌─────────────────────────────────────────────────────────────────────┐
│                          ARQUITECTURA GENERAL                        │
└─────────────────────────────────────────────────────────────────────┘

USUARIO (ADMIN)
    ├── Crea DIVISIONES
    ├── Crea PROGRAMAS
    ├── Crea USUARIOS con ROLES
    └── Asigna COORDINADORES a División/Programa

USUARIO (COORDINADOR) 
    ├── Asigna PROFESORES a División/Programa
    └── Asigna ALUMNOS a División/Programa

USUARIO (PROFESOR)
    ├── Se asigna a División/Programa
    ├── Crea GRUPOS
    └── Asigna ASESORÍAS a sus grupos

USUARIO (ALUMNO)
    ├── Se asigna a División/Programa
    └── Puede pertenecer a GRUPOS

┌─────────────────────────────────────────────────────────────────────┐
│                          RELACIONES BD                               │
└─────────────────────────────────────────────────────────────────────┘

USUARIO (1) ─┬─── (1) ROL
             ├─── (1) PROFESOR_PERFIL
             ├─── (1) ALUMNO_PERFIL
             └─── (1) COORDINADOR_PERFIL

PROFESOR_PERFIL (N) ─┬─── (1) USUARIO
                     ├─── (1) DIVISION
                     ├─── (1) PROGRAMA
                     └─── (1) GRUPO (N)

ALUMNO_PERFIL (N) ──┬─── (1) USUARIO
                    ├─── (1) DIVISION
                    └─── (1) PROGRAMA

COORDINADOR_PERFIL (N) ──┬─── (1) USUARIO
                         ├─── (1) DIVISION
                         └─── (1) PROGRAMA

GRUPO (N) ─┬─── (1) PROFESOR_PERFIL
           ├─── (1) DIVISION
           └─── (1) PROGRAMA

DIVISION (1) ─┬─── (N) PROFESOR_PERFIL
              ├─── (N) ALUMNO_PERFIL
              ├─── (N) COORDINADOR_PERFIL
              └─── (N) GRUPO

PROGRAMA (1) ─┬─── (N) PROFESOR_PERFIL
              ├─── (N) ALUMNO_PERFIL
              ├─── (N) COORDINADOR_PERFIL
              └─── (N) GRUPO
```

## 📁 Estructura de Carpetas

```
ms-admin/
├── src/
│   ├── main/
│   │   ├── java/com/uteq/admin/
│   │   │   ├── MsAdminApplication.java
│   │   │   ├── entity/
│   │   │   │   ├── Usuario.java (mejorado)
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Division.java (nuevo)
│   │   │   │   ├── Programa.java (nuevo)
│   │   │   │   ├── Grupo.java (nuevo)
│   │   │   │   ├── ProfesorPerfil.java (mejorado)
│   │   │   │   ├── AlumnoPerfil.java (mejorado)
│   │   │   │   └── CoordinadorPerfil.java (nuevo)
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java (mejorado)
│   │   │   │   ├── RolRepository.java
│   │   │   │   ├── DivisionRepository.java (nuevo)
│   │   │   │   ├── ProgramaRepository.java (nuevo)
│   │   │   │   ├── GrupoRepository.java (nuevo)
│   │   │   │   ├── CoordinadorPerfilRepository.java (nuevo)
│   │   │   │   ├── ProfesorPerfilRepository.java
│   │   │   │   └── AlumnoPerfilRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UsuarioService.java (mejorado)
│   │   │   │   ├── DivisionService.java (nuevo)
│   │   │   │   ├── ProgramaService.java (nuevo)
│   │   │   │   ├── GrupoService.java (nuevo)
│   │   │   │   ├── CoordinadorPerfilService.java (nuevo)
│   │   │   │   ├── PerfilService.java
│   │   │   │   └── impl/
│   │   │   │       ├── UsuarioServiceImpl.java (mejorado)
│   │   │   │       ├── DivisionServiceImpl.java (nuevo)
│   │   │   │       ├── ProgramaServiceImpl.java (nuevo)
│   │   │   │       ├── GrupoServiceImpl.java (nuevo)
│   │   │   │       ├── CoordinadorPerfilServiceImpl.java (nuevo)
│   │   │   │       └── PerfilServiceImpl.java
│   │   │   ├── controller/
│   │   │   │   ├── UsuarioController.java (mejorado)
│   │   │   │   ├── DivisionController.java (nuevo)
│   │   │   │   ├── ProgramaController.java (nuevo)
│   │   │   │   ├── GrupoController.java (nuevo)
│   │   │   │   ├── CoordinadorPerfilController.java (nuevo)
│   │   │   │   ├── PerfilController.java
│   │   │   │   ├── RolController.java
│   │   │   │   └── CoordinadorController.java
│   │   │   ├── dto/
│   │   │   │   ├── UsuarioDTO.java (existente)
│   │   │   │   ├── UsuarioCrearDTO.java (nuevo)
│   │   │   │   ├── DivisionDTO.java (existente)
│   │   │   │   ├── ProgramaDTO.java (existente)
│   │   │   │   ├── GrupoDTO.java (nuevo)
│   │   │   │   ├── CoordinadorPerfilDTO.java (nuevo)
│   │   │   │   ├── ProfesorPerfilDTO.java (nuevo)
│   │   │   │   └── AlumnoPerfilDTO.java (nuevo)
│   │   │   └── config/
│   │   └── resources/
│   │       └── application.yml (mejorado)
│   └── test/
├── pom.xml (sin cambios)
├── Dockerfile (por crear)
├── .dockerignore (por crear)
└── README.md
```

## 🔑 Cambios Principales

### ✅ Entidades Mejoradas
- `Usuario`: Agregados campos apellido, fechaActualizacion, ultimoAcceso
- `ProfesorPerfil`: Cambio de Long a FK directo de Division y Programa
- `AlumnoPerfil`: Cambio de Long a FK directo de Division y Programa

### ✅ Nuevas Entidades
- `Division`: Gestiona divisiones/grados
- `Programa`: Gestiona programas educativos
- `CoordinadorPerfil`: Asigna coordinadores a División/Programa
- `Grupo`: Agrupa alumnos por profesor, división y programa

### ✅ Nuevos Servicios (CRUD Completo)
- `DivisionService/Impl`
- `ProgramaService/Impl`
- `GrupoService/Impl`
- `CoordinadorPerfilService/Impl`

### ✅ Nuevos Controladores
- `DivisionController`
- `ProgramaController`
- `GrupoController`
- `CoordinadorPerfilController`

### ✅ Mejoras en Servicios Existentes
- `UsuarioService`: Métodos adicionales (crearDTO, actualizar, listarPorRol, listarActivos, eliminar)
- `UsuarioServiceImpl`: Implementación completa con conversión de DTOs

### ✅ Nuevos Repositorios
- `DivisionRepository`
- `ProgramaRepository`
- `GrupoRepository`
- `CoordinadorPerfilRepository`
- `UsuarioRepository`: Métodos adicionales (findByRol, findByActivo)

### ✅ Nuevos DTOs
- `UsuarioCrearDTO`: Para creación de usuarios
- `GrupoDTO`: Para CRUD de grupos
- `CoordinadorPerfilDTO`: Para CRUD de coordinadores
- `ProfesorPerfilDTO`: Para CRUD de profesores
- `AlumnoPerfilDTO`: Para CRUD de alumnos

## 📋 CRUD Endpoints Disponibles

### Usuarios (8 endpoints)
- POST /api/admin/usuarios
- GET /api/admin/usuarios
- GET /api/admin/usuarios/{id}
- PUT /api/admin/usuarios/{id}
- PUT /api/admin/usuarios/{id}/estado
- DELETE /api/admin/usuarios/{id}
- GET /api/admin/usuarios/rol/{rolId}
- GET /api/admin/usuarios/activos/listar
- POST /api/admin/usuarios/login

### Divisiones (7 endpoints)
- POST /api/divisiones
- GET /api/divisiones
- GET /api/divisiones/{id}
- PUT /api/divisiones/{id}
- PUT /api/divisiones/{id}/desactivar
- DELETE /api/divisiones/{id}
- GET /api/divisiones/activos/listar

### Programas (7 endpoints)
- POST /api/programas
- GET /api/programas
- GET /api/programas/{id}
- PUT /api/programas/{id}
- PUT /api/programas/{id}/desactivar
- DELETE /api/programas/{id}
- GET /api/programas/activos/listar

### Grupos (10 endpoints)
- POST /api/grupos
- GET /api/grupos
- GET /api/grupos/{id}
- PUT /api/grupos/{id}
- PUT /api/grupos/{id}/desactivar
- DELETE /api/grupos/{id}
- GET /api/grupos/activos/listar
- GET /api/grupos/profesor/{profesorId}
- GET /api/grupos/division/{divisionId}
- GET /api/grupos/programa/{programaId}

### Coordinadores (10 endpoints)
- POST /api/coordinadores
- GET /api/coordinadores
- GET /api/coordinadores/{id}
- PUT /api/coordinadores/{id}
- PUT /api/coordinadores/{id}/desactivar
- DELETE /api/coordinadores/{id}
- GET /api/coordinadores/activos/listar
- GET /api/coordinadores/usuario/{usuarioId}
- GET /api/coordinadores/division/{divisionId}
- GET /api/coordinadores/programa/{programaId}

## 🎯 Próximos Pasos

1. **MS-AUTH**: Crear microservicio de autenticación con JWT
2. **Seguridad**: Agregar @EnableWebSecurity y validar tokens
3. **Sincronización**: Implementar eventos entre microservicios
4. **MS-Asesorías**: Crear servicio de asesorías
5. **Docker**: Crear Dockerfiles para cada servicio
6. **Docker-Compose**: Orquestar todos los servicios
7. **Tests**: Agregar tests unitarios e integración
8. **Logging**: Centralizador con ELK o similares

## 📝 Notas Importantes

- La BD H2 está configurada en memoria (útil para desarrollo)
- Para producción, cambiar a PostgreSQL
- Los campos `fechaCreacion` y `fechaActualizacion` se llenan automáticamente
- El campo `ultimoAcceso` se actualiza en cada login
- Todos los rolnames deben ser: ADMIN, COORDINADOR, PROFESOR, ALUMNO
- Los DTOs protegen de cambios directos en entidades
