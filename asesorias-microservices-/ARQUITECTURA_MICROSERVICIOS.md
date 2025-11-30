# 📋 ARQUITECTURA POR MICROSERVICIO

## 🏛️ Distribución de Responsabilidades

### 1️⃣ **MS-ADMIN** (Puerto 8081)
**Responsable de**: Gestión de usuarios, roles, divisiones, programas y grupos

**Entidades**:
- Usuario
- Rol
- Division
- Programa
- Grupo
- ProfesorPerfil
- AlumnoPerfil
- CoordinadorPerfil

**Endpoints Clave**:
- `/api/admin/usuarios` - CRUD de usuarios
- `/api/divisiones` - CRUD de divisiones
- `/api/programas` - CRUD de programas
- `/api/grupos` - CRUD de grupos
- `/api/coordinadores` - CRUD de coordinadores

**Capacidades**:
- Crear/editar/eliminar usuarios con roles
- Asignar coordinadores a divisiones y programas
- Crear grupos
- Gestionar divisiones y programas

---

### 2️⃣ **MS-ASESORIAS** (Puerto 8082)
**Responsable de**: Gestión de asesorías y disponibilidades

**Entidades**:
- Asesoria (profesorId, alumnoId, fecha, hora, materia, estatus)
- Disponibilidad (profesorId, fecha, horaInicio, horaFin)

**Endpoints Clave**:
- `/api/asesorias` - CRUD de asesorías
- `/api/asesorias/profesor/{id}` - Asesorías de un profesor
- `/api/asesorias/alumno/{id}` - Asesorías de un alumno
- `/api/disponibilidades` - CRUD de disponibilidades
- `/api/disponibilidades/profesor/{id}` - Disponibilidades de profesor

**Capacidades**:
- Crear asesorías (con disponibilidadId o directamente con fecha/hora)
- Gestionar disponibilidades de profesores
- Listar asesorías por profesor o alumno
- Cambiar estatus de asesorías

**Sincronización**: 
- Copia de datos de División y Programa desde MS-Divisiones
- Referencia a usuarioId de profesores y alumnos

---

### 3️⃣ **MS-COORDINADORES** (Puerto 8083)
**Responsable de**: Coordinación entre divisiones/programas y asignaciones

**Entidades**:
- Asignacion (coordinadorId, divisionId, programaId)

**Endpoints Clave**:
- `/api/asignaciones` - CRUD de asignaciones
- `/api/asignaciones/coordinador/{id}` - Por coordinador
- `/api/asignaciones/division/{id}` - Por división

**Capacidades**:
- Asignar coordinadores a divisiones y programas
- Validar permisos de coordinador
- Listar responsabilidades de coordinador

**Sincronización**:
- Replica de Coordinador desde MS-Admin
- Replica de Division/Programa desde MS-Divisiones

---

### 4️⃣ **MS-DIVISIONES** (Puerto 8084)
**Responsable de**: Gestión de divisiones y programas educativos

**Entidades**:
- Division (clave, nombre, descripción)
- ProgramaEducativo (clave, nombre, descripción, divisionId)

**Endpoints Clave**:
- `/api/divisiones` - CRUD de divisiones
- `/api/divisiones/{id}/programas` - Programas de una división
- `/api/programas` - CRUD de programas

**Capacidades**:
- Crear/editar/eliminar divisiones
- Crear/editar/eliminar programas educativos
- Listar divisiones con sus programas
- Validar relaciones división-programa

**Nota**: Este MS es complementario a MS-Admin. Se puede considerar que MS-Admin es la fuente de verdad de División/Programa, pero MS-Divisiones proporciona queries específicas.

---

### 5️⃣ **MS-PROFESORES** (Puerto 8085)
**Responsable de**: Gestión y datos específicos de profesores

**Entidades**:
- Profesor (usuarioId, divisionId, programaId, especialidad)

**Endpoints Clave**:
- `/api/profesores` - CRUD de profesores
- `/api/profesores/{id}/perfil` - Perfil completo
- `/api/profesores/{id}/asesorias` - Asesorías del profesor
- `/api/profesores/{id}/disponibilidades` - Disponibilidades
- `/api/profesores/{usuarioId}/asesorias` - Crear asesoría

**Capacidades**:
- Obtener datos de profesor
- Ver asesorías del profesor
- Crear disponibilidades
- Ver perfil completo (usuario + profesor + asesorías)

**Sincronización**:
- Copia de Usuario desde MS-Admin
- Copia de Division/Programa desde MS-Divisiones
- Referencia a Asesoría desde MS-Asesorias

---

### 6️⃣ **MS-ALUMNOS** (Puerto 8086)
**Responsable de**: Gestión y datos específicos de alumnos

**Entidades**:
- Alumno (usuarioId, divisionId, programaId, matricula)

**Endpoints Clave**:
- `/api/alumnos` - CRUD de alumnos
- `/api/alumnos/{id}/perfil` - Perfil completo
- `/api/alumnos/{id}/asesorias` - Asesorías del alumno
- `/api/alumnos/{usuarioId}/solicitar-asesoria` - Solicitar asesoría

**Capacidades**:
- Obtener datos de alumno
- Ver asesorías del alumno
- Solicitar asesorías
- Ver perfil completo (usuario + alumno + asesorías)

**Sincronización**:
- Copia de Usuario desde MS-Admin
- Copia de Division/Programa desde MS-Divisiones
- Referencia a Asesoría desde MS-Asesorias

---

### 7️⃣ **MS-AUTH** (Puerto 8087) - POR CREAR
**Responsable de**: Autenticación y autorización con JWT

**Entidades**:
- Token (usuarioId, token, fechaExpiracion, tipo)
- Permiso (rolId, recurso, accion)

**Endpoints Clave**:
- `/api/auth/login` - Autenticación
- `/api/auth/refresh` - Renovar token
- `/api/auth/validate` - Validar token
- `/api/auth/logout` - Cerrar sesión

**Capacidades**:
- Generar JWT con claims (usuarioId, rol, permisos)
- Validar tokens
- Refrescar tokens
- Gestionar permisos por rol

**Validaciones**:
- Verificar existencia de usuario en MS-Admin
- Verificar rol
- Validar fecha de expiración

---

## 📡 Patrón de Sincronización de Datos

```
┌─────────────────┐
│    MS-Admin     │
│ (Fuente Verdad) │
└────────┬────────┘
         │
    ┌────┴────────────────────┐
    │                         │
    ▼                         ▼
┌──────────────┐         ┌──────────────┐
│MS-Divisiones │         │  MS-Asesorias│
│  (copia BD)  │         │   (copia BD) │
└──────────────┘         └──────────────┘
    │                         │
    └─────────────┬───────────┘
                  │
    ┌─────────────┼─────────────┐
    │             │             │
    ▼             ▼             ▼
┌──────────┐ ┌──────────┐ ┌──────────┐
│Profesores│ │ Alumnos  │ │Coordinad.│
└──────────┘ └──────────┘ └──────────┘
```

### Estrategia:
1. **MS-Admin** es la fuente de verdad (CRUD principal)
2. **MS-Divisiones** replica datos de divisiones/programas
3. **MS-Asesorias** replica datos de disponibilidades
4. **MS-Profesores/Alumnos/Coordinadores** replican usuarios y referencias
5. **Event-driven**: Cambios en MS-Admin → eventos → replica en otros MS

---

## 🔄 Flujo de Creación de Usuario (Ejemplo)

```
1. Admin crea Usuario en MS-Admin
   POST /api/admin/usuarios
   
2. MS-Admin genera evento: "USUARIO_CREADO"
   
3. Otros MS suscritos reciben evento:
   - MS-Coordinadores: Espera para crear CoordinadorPerfil
   - MS-Profesores: Espera para crear ProfesorPerfil
   - MS-Alumnos: Espera para crear AlumnoPerfil
   
4. Coordinador accede a MS-Coordinadores:
   POST /api/coordinadores
   → Busca usuario en MS-Admin
   → Crea perfil local
```

---

## 🔐 Flujo de Autenticación (Próximo paso)

```
1. Cliente hace login en MS-Auth:
   POST /api/auth/login
   {
     "correo": "usuario@uteq.edu",
     "password": "password123"
   }
   
2. MS-Auth valida en MS-Admin:
   GET /api/admin/usuarios/login
   
3. Si es válido:
   - Genera JWT con claims (id, email, rol)
   - Retorna token y refresh_token
   
4. Cliente incluye token en headers:
   Authorization: Bearer <token>
   
5. API Gateway valida token en MS-Auth:
   GET /api/auth/validate?token=<token>
   
6. Si es válido, permite acceso a recurso
   Si no, retorna 401 Unauthorized
```

---

## 📊 Matriz de Responsabilidades

| Feature | MS-Admin | MS-Divisiones | MS-Asesorias | MS-Profesores | MS-Alumnos | MS-Coordinadores | MS-Auth |
|---------|----------|---------------|--------------|---------------|-----------|------------------|---------|
| CRUD Usuarios | ✅ | - | - | - | - | - | ✓ |
| CRUD Divisiones | ✅ | ✅ | - | - | - | - | - |
| CRUD Programas | ✅ | ✅ | - | - | - | - | - |
| CRUD Roles | ✅ | - | - | - | - | - | - |
| CRUD Asesorias | - | - | ✅ | ✓ | ✓ | - | - |
| CRUD Disponibilidades | - | - | ✅ | ✓ | - | - | - |
| CRUD Profesores | ✅ | - | - | ✅ | - | - | - |
| CRUD Alumnos | ✅ | - | - | - | ✅ | - | - |
| CRUD Coordinadores | ✅ | - | - | - | - | ✅ | - |
| Autenticación | - | - | - | - | - | - | ✅ |
| Autorización | - | - | - | - | - | - | ✅ |
| JWT Tokens | - | - | - | - | - | - | ✅ |

Legend: ✅ = Responsable Principal, ✓ = Acceso/Lectura, - = No aplica

---

## 🛠️ Próximos Pasos por MS

### MS-Admin
- ✅ Completado (División, Programa, Grupo, Coordinador)
- [ ] Agregar validaciones
- [ ] Agregar transacciones

### MS-Asesorias
- [ ] Completar CRUD de Asesoria
- [ ] Completar CRUD de Disponibilidad
- [ ] Agregar estado (pendiente, confirmada, cancelada)
- [ ] Agregar notificaciones

### MS-Coordinadores
- [ ] Completar CRUD de Asignacion
- [ ] Agregar validaciones de permisos
- [ ] Agregar sincronización con MS-Admin

### MS-Divisiones
- [ ] Completar CRUD de Division
- [ ] Completar CRUD de Programa
- [ ] Agregar relaciones bidireccionales
- [ ] Agregar validaciones

### MS-Profesores
- [ ] Completar CRUD de Profesor
- [ ] Agregar sincronización con MS-Admin
- [ ] Agregar consultas complejas (asesorías, disponibilidades)

### MS-Alumnos
- [ ] Completar CRUD de Alumno
- [ ] Agregar sincronización con MS-Admin
- [ ] Agregar consultas complejas (asesorías solicitadas)

### MS-Auth (CREAR)
- [ ] Crear microservicio
- [ ] Implementar autenticación con JWT
- [ ] Implementar autorización por rol
- [ ] Agregar refresh token
- [ ] Agregar validación centralizada

---

**Creado**: 2025-01-15
**Versión**: 1.0.0
