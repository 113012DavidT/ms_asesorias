# 📍 Endpoints Actualizados - Sistema de Asesorías

## ✅ URLs Correctas con Prefijo /api

**Base URL**: `http://localhost:8000`

Todos los endpoints están bajo el prefijo `/api/` en el API Gateway.

---

## 🔐 AUTENTICACIÓN

### Login
```
POST /api/auth/login
Headers: Content-Type: application/json
Body: {
  "correoMatricula": "admin@uteq.edu",
  "password": "admin123"
}
Response: {
  "token": "...",
  "refreshToken": "...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Admin",
  "rolNombre": "ADMIN",
  "expiresIn": 86400
}
```

### Refrescar Token
```
POST /api/auth/refresh
Headers: Authorization: Bearer {refreshToken}
Response: {
  "token": "...",
  "refreshToken": "...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Admin",
  "rolNombre": "ADMIN"
}
```

### Validar Token
```
POST /api/auth/validate
Headers: Authorization: Bearer {token}
Response: {
  "valid": true,
  "usuarioId": 1,
  "correoMatricula": "admin@uteq.edu",
  "rolNombre": "ADMIN"
}
```

---

## 👥 USUARIOS (MS-ADMIN)

```
GET    /api/admin/usuarios          - Listar usuarios
GET    /api/admin/usuarios/{id}     - Obtener usuario
POST   /api/admin/usuarios          - Crear usuario
PUT    /api/admin/usuarios/{id}     - Editar usuario
DELETE /api/admin/usuarios/{id}     - Eliminar usuario
```

---

## 📚 DIVISIONES

```
GET    /api/divisiones              - Listar divisiones
GET    /api/divisiones/{id}         - Obtener división
POST   /api/divisiones              - Crear división
PUT    /api/divisiones/{id}         - Editar división
DELETE /api/divisiones/{id}         - Eliminar división
```

---

## 📖 PROGRAMAS

```
GET    /api/programas               - Listar programas
GET    /api/programas/{id}          - Obtener programa
POST   /api/programas               - Crear programa
PUT    /api/programas/{id}          - Editar programa
DELETE /api/programas/{id}          - Eliminar programa
```

---

## 🎓 PROFESORES

```
GET    /api/profesores              - Listar profesores
GET    /api/profesores/{id}         - Obtener profesor
POST   /api/profesores              - Crear profesor
PUT    /api/profesores/{id}         - Editar profesor
DELETE /api/profesores/{id}         - Eliminar profesor
GET    /api/profesores/division/{id} - Profesores por división
GET    /api/profesores/programa/{id} - Profesores por programa
```

---

## 👨‍🎓 ALUMNOS

```
GET    /api/alumnos                 - Listar alumnos
GET    /api/alumnos/{id}            - Obtener alumno
POST   /api/alumnos                 - Crear alumno
PUT    /api/alumnos/{id}            - Editar alumno
DELETE /api/alumnos/{id}            - Eliminar alumno
GET    /api/alumnos/division/{id}   - Alumnos por división
GET    /api/alumnos/programa/{id}   - Alumnos por programa
PATCH  /api/alumnos/{id}/promedio   - Actualizar promedio
```

---

## 📋 ASESORÍAS

```
GET    /api/asesorias               - Listar asesorías
GET    /api/asesorias/{id}          - Obtener asesoría
POST   /api/asesorias               - Crear asesoría
PUT    /api/asesorias/{id}          - Editar asesoría
DELETE /api/asesorias/{id}          - Eliminar asesoría
GET    /api/asesorias/profesor/{id} - Asesorías del profesor
GET    /api/asesorias/alumno/{id}   - Asesorías del alumno
GET    /api/asesorias/grupo/{id}    - Asesorías del grupo
```

---

## 👥 COORDINADORES

```
GET    /api/coordinadores           - Listar coordinadores
GET    /api/coordinadores/{id}      - Obtener coordinador
POST   /api/coordinadores           - Crear coordinador
PUT    /api/coordinadores/{id}      - Editar coordinador
DELETE /api/coordinadores/{id}      - Eliminar coordinador
GET    /api/coordinadores/division/{id}  - Por división
GET    /api/coordinadores/programa/{id}  - Por programa
```

---

## 👥 ROLES

```
GET    /api/admin/roles             - Listar roles
GET    /api/admin/roles/{id}        - Obtener rol
```

---

## 📍 GRUPOS

```
GET    /api/grupos                  - Listar grupos
GET    /api/grupos/{id}             - Obtener grupo
POST   /api/grupos                  - Crear grupo
PUT    /api/grupos/{id}             - Editar grupo
DELETE /api/grupos/{id}             - Eliminar grupo
```

---

## 🧑‍💼 PERFILES

### Coordinador
```
GET    /api/coordinador-perfiles    - Listar perfiles
GET    /api/coordinador-perfiles/{id}
POST   /api/coordinador-perfiles    - Crear perfil
PUT    /api/coordinador-perfiles/{id}
DELETE /api/coordinador-perfiles/{id}
```

### Profesor
```
GET    /api/profesor-perfiles       - Listar perfiles
GET    /api/profesor-perfiles/{id}
POST   /api/profesor-perfiles       - Crear perfil
PUT    /api/profesor-perfiles/{id}
DELETE /api/profesor-perfiles/{id}
```

### Alumno
```
GET    /api/alumno-perfiles         - Listar perfiles
GET    /api/alumno-perfiles/{id}
POST   /api/alumno-perfiles         - Crear perfil
PUT    /api/alumno-perfiles/{id}
DELETE /api/alumno-perfiles/{id}
```

---

## 📝 EJEMPLO DE USO EN EL FRONTEND

```javascript
// En los dashboards, los endpoints se usan así:

// DashboardAdmin.jsx
const usuariosAPI = useFetch('/api/admin/usuarios');
const divisionesAPI = useFetch('/api/divisiones');
const programasAPI = useFetch('/api/programas');

// DashboardCoordinador.jsx
const profesoresAPI = useFetch('/api/profesores');
const alumnosAPI = useFetch('/api/alumnos');

// DashboardProfesor.jsx
const gruposAPI = useFetch('/api/grupos');
const asesoriasAPI = useFetch('/api/asesorias');

// DashboardAlumno.jsx
const asesoriasAPI = useFetch('/api/asesorias');
const profesoresAPI = useFetch('/api/profesores');
```

---

## ⚙️ CONFIGURACIÓN EN AXIOSCONFIG

El archivo `axiosConfig.js` ya está configurado para:
- ✅ Agregar token automáticamente en header Authorization
- ✅ Refrescar token si expira (401)
- ✅ Redirigir a login si falla el refresh
- ✅ BaseURL: http://localhost:8000

---

## 🔑 HEADERS REQUERIDOS

**Todos los endpoints (excepto login) requieren:**
```javascript
Authorization: Bearer {token}
Content-Type: application/json
```

El `axiosConfig.js` añade estos automáticamente.

---

## ✅ ACTUALIZACIONES REALIZADAS

- ✅ AuthContext.jsx - Usa `/api/auth/login`
- ✅ DashboardAdmin.jsx - Usa `/api/admin/usuarios`, `/api/divisiones`, `/api/programas`
- ✅ DashboardCoordinador.jsx - Usa `/api/profesores`, `/api/alumnos`
- ✅ DashboardProfesor.jsx - Usa `/api/grupos`, `/api/asesorias`
- ✅ DashboardAlumno.jsx - Usa `/api/asesorias`, `/api/profesores`
- ✅ axiosConfig.js - Manejo de refresh token mejorado
- ✅ useFetch.js - Listo para usar con nuevos endpoints

---

**Última actualización**: 25 de Noviembre de 2025
**Estado**: ✅ Endpoints Actualizados
