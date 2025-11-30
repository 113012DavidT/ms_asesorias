# 🎯 ENDPOINTS PARA EL FRONTEND

## ⚠️ IMPORTANTE

**Todas las peticiones DEBEN ir al API Gateway en puerto 8000**

```javascript
const API_BASE_URL = "http://localhost:8000";
```

---

## 🔐 AUTENTICACIÓN

### 1️⃣ Login - OBTENER TOKEN

**Endpoint:** `POST /api/auth/login`

```javascript
// Ejemplo con Axios
const response = await axios.post(
  `${API_BASE_URL}/api/auth/login`,
  {
    correoMatricula: "admin@uteq.edu",
    password: "admin123"
  },
  {
    headers: {
      "Content-Type": "application/json"
    }
  }
);

// Response exitoso (200 OK)
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Usuario Prueba",
  "rolNombre": "ADMIN",
  "expiresIn": 86400  // segundos (24 horas)
}

// Guardar el token en localStorage o sessionStorage
localStorage.setItem("token", response.data.token);
localStorage.setItem("refreshToken", response.data.refreshToken);
localStorage.setItem("user", JSON.stringify({
  usuarioId: response.data.usuarioId,
  nombre: response.data.nombre,
  rolNombre: response.data.rolNombre
}));
```

### 2️⃣ Usar el Token en Siguientes Requests

```javascript
// Agregar token en todas las peticiones autenticadas
const config = {
  headers: {
    "Authorization": `Bearer ${localStorage.getItem("token")}`,
    "Content-Type": "application/json"
  }
};

axios.get(`${API_BASE_URL}/api/admin/usuarios`, config);
```

### 3️⃣ Validar Token (Opcional)

**Endpoint:** `POST /api/auth/validate`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/auth/validate`,
  {
    token: localStorage.getItem("token")
  },
  {
    headers: {
      "Content-Type": "application/json"
    }
  }
);

// Response exitoso (200 OK)
{
  "valid": true,
  "usuarioId": 1,
  "correoMatricula": "admin@uteq.edu",
  "rolNombre": "ADMIN",
  "mensaje": "Token válido"
}
```

### 4️⃣ Refrescar Token (Cuando Expire)

**Endpoint:** `POST /api/auth/refresh`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/auth/refresh`,
  {},
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("refreshToken")}`,
      "Content-Type": "application/json"
    }
  }
);

// Response exitoso (200 OK)
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Usuario Prueba",
  "rolNombre": "ADMIN"
}

// Actualizar token
localStorage.setItem("token", response.data.token);
localStorage.setItem("refreshToken", response.data.refreshToken);
```

---

## 👥 MS-ADMIN - GESTIÓN DE USUARIOS

### Listar Usuarios

**Endpoint:** `GET /api/admin/usuarios`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/admin/usuarios`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "correoMatricula": "admin@uteq.edu",
    "nombre": "Usuario Prueba",
    "rolId": 1,
    "rolNombre": "ADMIN",
    "activo": true
  },
  ...
]
```

### Crear Usuario

**Endpoint:** `POST /api/admin/usuarios`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/admin/usuarios`,
  {
    correoMatricula: "nuevo@uteq.edu",
    nombre: "Nuevo Usuario",
    password: "password123",
    rolId: 2
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);
```

---

## 📚 MS-DIVISIONES - GESTIÓN DE DIVISIONES

### Listar Divisiones

**Endpoint:** `GET /api/divisiones`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/divisiones`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "clave": "DIV-1732534000000",
    "nombre": "1ro A",
    "descripcion": "Primer año paralela A",
    "programas": []
  },
  ...
]
```

### Crear División

**Endpoint:** `POST /api/divisiones`

⚠️ **IMPORTANTE:** El endpoint requiere `nombre` y opcionalmente `descripcion`. La `clave` se genera automáticamente.

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/divisiones`,
  {
    nombre: "3ro C",
    descripcion: "Tercer año paralela C"
    // NO incluir: activo, clave, id - se generan automáticamente
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);

// Response exitoso (201 Created)
{
  "id": 7,
  "clave": "DIV-1732534800000",
  "nombre": "3ro C",
  "descripcion": "Tercer año paralela C",
  "programas": []
}
```

### Listar Programas

**Endpoint:** `GET /api/programas`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/programas`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "clave": "PROG-1732534100000",
    "nombre": "Ingeniería en Sistemas",
    "descripcion": "Carrera de Ingeniería en Sistemas Computacionales"
  },
  ...
]
```

### Crear Programa

**Endpoint:** `POST /api/programas`

⚠️ **IMPORTANTE:** 
- `nombre` → **OBLIGATORIO**
- `descripcion` → opcional
- `division.id` → **OBLIGATORIO** (debe estar asignado a una división)
- La `clave` se genera automáticamente

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/programas`,
  {
    nombre: "Medicina",
    descripcion: "Carrera de Medicina",
    division: {
      "id": 1  // ← ID de la división a la que pertenece (REQUERIDO)
    }
    // NO incluir: clave, id - se generan automáticamente
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);

// Response exitoso (201 Created)
{
  "id": 6,
  "clave": "PROG-1732534900000",
  "nombre": "Medicina",
  "descripcion": "Carrera de Medicina"
}
```

---

## 🎓 MS-PROFESORES - GESTIÓN DE PROFESORES

### Listar Profesores

**Endpoint:** `GET /api/profesores`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/profesores`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "nombre": "Prof. Juan",
    "correoMatricula": "profesor1@uteq.edu",
    "especialidad": "Matemáticas",
    "activo": true
  },
  ...
]
```

### Obtener Profesor por ID

**Endpoint:** `GET /api/profesores/{id}`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/profesores/1`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);
```

### Crear Profesor

**Endpoint:** `POST /api/profesores`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/profesores`,
  {
    nombre: "Prof. María",
    correoMatricula: "maria@uteq.edu",
    especialidad: "Física",
    activo: true
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);
```

---

## 👨‍🎓 MS-ALUMNOS - GESTIÓN DE ALUMNOS

### Listar Alumnos

**Endpoint:** `GET /api/alumnos`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/alumnos`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "nombre": "Alumno 1",
    "correoMatricula": "alumno1@uteq.edu",
    "divisioId": 1,
    "activo": true
  },
  ...
]
```

### Obtener Alumno por ID

**Endpoint:** `GET /api/alumnos/{id}`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/alumnos/1`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);
```

### Crear Alumno

**Endpoint:** `POST /api/alumnos`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/alumnos`,
  {
    nombre: "Nuevo Alumno",
    correoMatricula: "nuevo.alumno@uteq.edu",
    divisioId: 1,
    activo: true
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);
```

---

## 📋 MS-ASESORIAS - GESTIÓN DE ASESORÍAS

### Listar Asesorías

**Endpoint:** `GET /api/asesorias`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/asesorias`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "profesorId": 1,
    "alumnoId": 1,
    "fecha": "2025-01-20",
    "hora": "14:00",
    "tema": "Consulta sobre proyecto",
    "estado": "pendiente"
  },
  ...
]
```

### Crear Asesoría

**Endpoint:** `POST /api/asesorias`

```javascript
const response = await axios.post(
  `${API_BASE_URL}/api/asesorias`,
  {
    profesorId: 1,
    alumnoId: 1,
    fecha: "2025-01-25",
    hora: "15:00",
    tema: "Ayuda con tareas",
    estado": "pendiente"
  },
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`,
      "Content-Type": "application/json"
    }
  }
);
```

### Listar Disponibilidades

**Endpoint:** `GET /api/disponibilidades`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/disponibilidades`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "profesorId": 1,
    "diaSemana": "LUNES",
    "horaInicio": "14:00",
    "horaFin": "16:00"
  },
  ...
]
```

---

## 👔 MS-COORDINADORES - GESTIÓN DE COORDINADORES

### Listar Coordinadores

**Endpoint:** `GET /api/coordinadores`

```javascript
const response = await axios.get(
  `${API_BASE_URL}/api/coordinadores`,
  {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  }
);

// Response
[
  {
    "id": 1,
    "nombre": "Coordinador 1",
    "correoMatricula": "coordinador1@uteq.edu",
    "divisioId": 1,
    "activo": true
  },
  ...
]
```

---

## ⚡ INTERCEPTOR RECOMENDADO (Axios)

```javascript
// configApi.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000'
});

// Interceptor para agregar token automáticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para manejar token expirado
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(
          'http://localhost:8000/api/auth/refresh',
          {},
          {
            headers: {
              'Authorization': `Bearer ${refreshToken}`
            }
          }
        );

        localStorage.setItem('token', response.data.token);
        localStorage.setItem('refreshToken', response.data.refreshToken);

        originalRequest.headers.Authorization = `Bearer ${response.data.token}`;
        return api(originalRequest);
      } catch (refreshError) {
        // Token refresh falló, redirigir a login
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;

// Uso en componentes
import api from './configApi';

const usuarios = await api.get('/api/admin/usuarios');
```

---

## 🐛 ERRORES COMUNES Y SOLUCIONES

### Error 404 - Not Found
```
❌ Problema: POST http://localhost:8000/auth/login 404
✅ Solución: Debe ser POST http://localhost:8000/api/auth/login
```

### Error 401 - Unauthorized
```
❌ Problema: No hay token o token expirado
✅ Solución: 
  1. Primero hacer login
  2. Incluir Authorization header: Bearer {token}
  3. Refrescar token si expiró
```

### Error 403 - Forbidden
```
❌ Problema: Ruta no permitida o rol insuficiente
✅ Solución: Verificar permisos del usuario y roles
```

### Error 500 - Internal Server Error
```
❌ Problema: Error en el servidor
✅ Solución: Revisar logs del backend
```

---

## 📝 CREDENCIALES DE PRUEBA

| Usuario | Email | Password | Rol |
|---------|-------|----------|-----|
| Admin | admin@uteq.edu | admin123 | ADMIN |
| Coordinador | coordinador1@uteq.edu | pass123 | COORDINADOR |
| Profesor | profesor1@uteq.edu | pass123 | PROFESOR |
| Alumno | alumno1@uteq.edu | pass123 | ALUMNO |

---

**Última actualización:** 2025-11-25
**Versión:** 1.0.0
