# 📚 Documentación de Endpoints - Sistema de Asesorías

## 🔐 Autenticación Base

**URL Base para desarrollo**: `http://localhost:8000` (API Gateway)

Todos los endpoints (excepto login) requieren un token JWT en el header:
```
Authorization: Bearer <token>
```

---

## 🔓 **MS-AUTH (Puerto 8088)** - Autenticación

### 1. Login
**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "correoMatricula": "admin@uteq.edu",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "correoMatricula": "admin@uteq.edu",
  "nombre": "Juan",
  "apellido": "Admin",
  "rolId": 1,
  "rolNombre": "ADMIN"
}
```

**Respuestas posibles:**
- `200 OK` - Login exitoso, token generado
- `401 Unauthorized` - Credenciales inválidas
- `400 Bad Request` - Campos faltantes

---

## 👥 **MS-ADMIN (Puerto 8081)** - Gestión Administrativa

### Roles
**1. Listar todos los roles**
- **Endpoint:** `GET /roles`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "ADMIN"
  },
  {
    "id": 2,
    "nombre": "COORDINADOR"
  },
  {
    "id": 3,
    "nombre": "PROFESOR"
  },
  {
    "id": 4,
    "nombre": "ALUMNO"
  }
]
```

**2. Obtener rol por ID**
- **Endpoint:** `GET /roles/{id}`
- **Parameters:** `id` (Long)
- **Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "ADMIN"
}
```

---

### Usuarios
**1. Listar todos los usuarios**
- **Endpoint:** `GET /usuarios`
- **Headers:** `Authorization: Bearer <token>`
- **Query Parameters (opcionales):**
  - `page` (int, default: 0)
  - `size` (int, default: 20)
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "correoMatricula": "admin@uteq.edu",
    "nombre": "Juan",
    "apellido": "Admin",
    "activo": true,
    "rolId": 1,
    "rolNombre": "ADMIN",
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener usuario por ID**
- **Endpoint:** `GET /usuarios/{id}`
- **Parameters:** `id` (Long)
- **Response (200 OK):**
```json
{
  "id": 1,
  "correoMatricula": "admin@uteq.edu",
  "nombre": "Juan",
  "apellido": "Admin",
  "activo": true,
  "rolId": 1,
  "rolNombre": "ADMIN",
  "fechaCreacion": "2025-01-15"
}
```

**3. Crear usuario**
- **Endpoint:** `POST /usuarios`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
```json
{
  "correoMatricula": "profesor1@uteq.edu",
  "password": "pass123",
  "nombre": "Pedro",
  "apellido": "Martínez",
  "activo": true,
  "rolId": 3
}
```
- **Response (201 Created):**
```json
{
  "id": 4,
  "correoMatricula": "profesor1@uteq.edu",
  "nombre": "Pedro",
  "apellido": "Martínez",
  "activo": true,
  "rolId": 3,
  "rolNombre": "PROFESOR",
  "fechaCreacion": "2025-11-25"
}
```

**4. Actualizar usuario**
- **Endpoint:** `PUT /usuarios/{id}`
- **Parameters:** `id` (Long)
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
```json
{
  "nombre": "Pedro",
  "apellido": "Martínez García",
  "activo": true
}
```
- **Response (200 OK):** Mismo formato que GET

**5. Eliminar usuario**
- **Endpoint:** `DELETE /usuarios/{id}`
- **Parameters:** `id` (Long)
- **Headers:** `Authorization: Bearer <token>`
- **Response (204 No Content)**

---

### Divisiones
**1. Listar todas las divisiones**
- **Endpoint:** `GET /divisiones`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "1ro A",
    "descripcion": "Primer año paralela A",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  },
  {
    "id": 2,
    "nombre": "1ro B",
    "descripcion": "Primer año paralela B",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener división por ID**
- **Endpoint:** `GET /divisiones/{id}`
- **Parameters:** `id` (Long)
- **Response (200 OK):** Mismo formato que arriba

**3. Crear división**
- **Endpoint:** `POST /divisiones`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
```json
{
  "nombre": "3ro A",
  "descripcion": "Tercer año paralela A",
  "activo": true
}
```
- **Response (201 Created):** Mismo formato

**4. Actualizar división**
- **Endpoint:** `PUT /divisiones/{id}`
- **Parameters:** `id` (Long)
- **Request Body:** Campos a actualizar
- **Response (200 OK)**

**5. Eliminar división**
- **Endpoint:** `DELETE /divisiones/{id}`
- **Response (204 No Content)**

---

### Programas
**1. Listar todos los programas**
- **Endpoint:** `GET /programas`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Ingeniería en Sistemas",
    "descripcion": "Carrera de 4 años en Ingeniería de Sistemas",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener programa por ID**
- **Endpoint:** `GET /programas/{id}`
- **Parameters:** `id` (Long)
- **Response (200 OK)**

**3. Crear programa**
- **Endpoint:** `POST /programas`
- **Request Body:**
```json
{
  "nombre": "Derecho",
  "descripcion": "Carrera de Derecho",
  "activo": true
}
```

**4. Actualizar programa**
- **Endpoint:** `PUT /programas/{id}`

**5. Eliminar programa**
- **Endpoint:** `DELETE /programas/{id}`

---

### Perfiles de Coordinador
**1. Listar perfiles de coordinadores**
- **Endpoint:** `GET /coordinador-perfiles`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 2,
    "divisionId": 1,
    "programaId": 1,
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener perfil coordinador por ID**
- **Endpoint:** `GET /coordinador-perfiles/{id}`
- **Response (200 OK)**

**3. Crear perfil coordinador**
- **Endpoint:** `POST /coordinador-perfiles`
- **Request Body:**
```json
{
  "usuarioId": 2,
  "divisionId": 1,
  "programaId": 1,
  "activo": true
}
```

**4. Actualizar perfil coordinador**
- **Endpoint:** `PUT /coordinador-perfiles/{id}`

**5. Eliminar perfil coordinador**
- **Endpoint:** `DELETE /coordinador-perfiles/{id}`

---

### Perfiles de Profesor
**1. Listar perfiles de profesores**
- **Endpoint:** `GET /profesor-perfiles`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 4,
    "divisionId": 1,
    "programaId": 1,
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener perfil profesor por ID**
- **Endpoint:** `GET /profesor-perfiles/{id}`

**3. Crear perfil profesor**
- **Endpoint:** `POST /profesor-perfiles`
- **Request Body:**
```json
{
  "usuarioId": 4,
  "divisionId": 1,
  "programaId": 1,
  "activo": true
}
```

**4. Actualizar perfil profesor**
- **Endpoint:** `PUT /profesor-perfiles/{id}`

**5. Eliminar perfil profesor**
- **Endpoint:** `DELETE /profesor-perfiles/{id}`

---

### Perfiles de Alumno
**1. Listar perfiles de alumnos**
- **Endpoint:** `GET /alumno-perfiles`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 7,
    "divisionId": 1,
    "programaId": 1,
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener perfil alumno por ID**
- **Endpoint:** `GET /alumno-perfiles/{id}`

**3. Crear perfil alumno**
- **Endpoint:** `POST /alumno-perfiles`
- **Request Body:**
```json
{
  "usuarioId": 7,
  "divisionId": 1,
  "programaId": 1,
  "activo": true
}
```

**4. Actualizar perfil alumno**
- **Endpoint:** `PUT /alumno-perfiles/{id}`

**5. Eliminar perfil alumno**
- **Endpoint:** `DELETE /alumno-perfiles/{id}`

---

### Grupos
**1. Listar todos los grupos**
- **Endpoint:** `GET /grupos`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Grupo 1 - Matemáticas",
    "descripcion": "Grupo de tutoría de Matemáticas I",
    "profesorId": 1,
    "divisionId": 1,
    "programaId": 1,
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener grupo por ID**
- **Endpoint:** `GET /grupos/{id}`

**3. Crear grupo**
- **Endpoint:** `POST /grupos`
- **Request Body:**
```json
{
  "nombre": "Grupo 5 - Cálculo",
  "descripcion": "Grupo de tutoría de Cálculo II",
  "profesorId": 1,
  "divisionId": 2,
  "programaId": 1,
  "activo": true
}
```

**4. Actualizar grupo**
- **Endpoint:** `PUT /grupos/{id}`

**5. Eliminar grupo**
- **Endpoint:** `DELETE /grupos/{id}`

---

## 🎓 **MS-ASESORIAS (Puerto 8082)** - Gestión de Asesorías

### Asesorías
**1. Listar todas las asesorías**
- **Endpoint:** `GET /asesorias`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Asesoría de Matemáticas",
    "descripcion": "Tutoría individual de Cálculo II",
    "profesorId": 1,
    "alumnoId": 7,
    "grupoId": 1,
    "fecha": "2025-11-30",
    "horaInicio": "10:00",
    "horaFin": "11:00",
    "estado": "PROGRAMADA",
    "fechaCreacion": "2025-11-25"
  }
]
```

**2. Obtener asesoría por ID**
- **Endpoint:** `GET /asesorias/{id}`

**3. Crear asesoría**
- **Endpoint:** `POST /asesorias`
- **Request Body:**
```json
{
  "titulo": "Asesoría de Programación",
  "descripcion": "Tutoría de Java Avanzado",
  "profesorId": 1,
  "alumnoId": 7,
  "grupoId": 1,
  "fecha": "2025-12-01",
  "horaInicio": "14:00",
  "horaFin": "15:00"
}
```
- **Response (201 Created)**

**4. Actualizar asesoría**
- **Endpoint:** `PUT /asesorias/{id}`
- **Request Body:** Campos a actualizar
```json
{
  "estado": "COMPLETADA"
}
```

**5. Eliminar asesoría**
- **Endpoint:** `DELETE /asesorias/{id}`

**6. Obtener asesorías por profesor**
- **Endpoint:** `GET /asesorias/profesor/{profesorId}`

**7. Obtener asesorías por alumno**
- **Endpoint:** `GET /asesorias/alumno/{alumnoId}`

**8. Obtener asesorías por grupo**
- **Endpoint:** `GET /asesorias/grupo/{grupoId}`

---

## 📋 **MS-PROFESORES (Puerto 8085)** - Gestión de Profesores

### Información de Profesores
**1. Listar todos los profesores**
- **Endpoint:** `GET /profesores`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 4,
    "divisionId": 1,
    "programaId": 1,
    "especialidad": "Matemáticas",
    "telefonoContacto": "+593987654321",
    "correoAlternativo": "pedro.martinez@gmail.com",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener profesor por ID**
- **Endpoint:** `GET /profesores/{id}`

**3. Crear profesor**
- **Endpoint:** `POST /profesores`
- **Request Body:**
```json
{
  "usuarioId": 4,
  "divisionId": 1,
  "programaId": 1,
  "especialidad": "Física",
  "telefonoContacto": "+593987654321",
  "correoAlternativo": "contact@example.com"
}
```

**4. Actualizar profesor**
- **Endpoint:** `PUT /profesores/{id}`

**5. Eliminar profesor**
- **Endpoint:** `DELETE /profesores/{id}`

**6. Obtener profesores por división**
- **Endpoint:** `GET /profesores/division/{divisionId}`

**7. Obtener profesores por programa**
- **Endpoint:** `GET /profesores/programa/{programaId}`

---

## 👨‍🎓 **MS-ALUMNOS (Puerto 8086)** - Gestión de Alumnos

### Información de Alumnos
**1. Listar todos los alumnos**
- **Endpoint:** `GET /alumnos`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 7,
    "divisionId": 1,
    "programaId": 1,
    "matricula": "ALU-2025-001",
    "promedio": 8.5,
    "estado": "ACTIVO",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener alumno por ID**
- **Endpoint:** `GET /alumnos/{id}`

**3. Crear alumno**
- **Endpoint:** `POST /alumnos`
- **Request Body:**
```json
{
  "usuarioId": 7,
  "divisionId": 1,
  "programaId": 1,
  "matricula": "ALU-2025-002",
  "promedio": 8.5,
  "estado": "ACTIVO"
}
```

**4. Actualizar alumno**
- **Endpoint:** `PUT /alumnos/{id}`

**5. Eliminar alumno**
- **Endpoint:** `DELETE /alumnos/{id}`

**6. Obtener alumnos por división**
- **Endpoint:** `GET /alumnos/division/{divisionId}`

**7. Obtener alumnos por programa**
- **Endpoint:** `GET /alumnos/programa/{programaId}`

**8. Actualizar promedio de alumno**
- **Endpoint:** `PATCH /alumnos/{id}/promedio`
- **Request Body:**
```json
{
  "promedio": 9.0
}
```

---

## 📊 **MS-COORDINADORES (Puerto 8083)** - Gestión de Coordinadores

### Información de Coordinadores
**1. Listar todos los coordinadores**
- **Endpoint:** `GET /coordinadores`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "usuarioId": 2,
    "divisionId": 1,
    "programaId": 1,
    "departamento": "Académico",
    "telefonoOficina": "+593987654321",
    "horarioAtencion": "Lunes a Viernes 8:00 - 17:00",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener coordinador por ID**
- **Endpoint:** `GET /coordinadores/{id}`

**3. Crear coordinador**
- **Endpoint:** `POST /coordinadores`
- **Request Body:**
```json
{
  "usuarioId": 2,
  "divisionId": 1,
  "programaId": 1,
  "departamento": "Académico",
  "telefonoOficina": "+593987654321",
  "horarioAtencion": "Lunes a Viernes 8:00 - 17:00"
}
```

**4. Actualizar coordinador**
- **Endpoint:** `PUT /coordinadores/{id}`

**5. Eliminar coordinador**
- **Endpoint:** `DELETE /coordinadores/{id}`

**6. Obtener coordinadores por división**
- **Endpoint:** `GET /coordinadores/division/{divisionId}`

**7. Obtener coordinadores por programa**
- **Endpoint:** `GET /coordinadores/programa/{programaId}`

---

## 🏢 **MS-DIVISIONES (Puerto 8084)** - Gestión de Divisiones

### Información de Divisiones
**1. Listar todas las divisiones**
- **Endpoint:** `GET /divisiones`
- **Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "1ro A",
    "descripcion": "Primer año paralela A",
    "cantidadEstudiantes": 35,
    "ubicacion": "Edificio A - Piso 1",
    "turno": "Matutino",
    "activo": true,
    "fechaCreacion": "2025-01-15"
  }
]
```

**2. Obtener división por ID**
- **Endpoint:** `GET /divisiones/{id}`

**3. Crear división**
- **Endpoint:** `POST /divisiones`
- **Request Body:**
```json
{
  "nombre": "4to A",
  "descripcion": "Cuarto año paralela A",
  "cantidadEstudiantes": 30,
  "ubicacion": "Edificio B - Piso 2",
  "turno": "Vespertino"
}
```

**4. Actualizar división**
- **Endpoint:** `PUT /divisiones/{id}`

**5. Eliminar división**
- **Endpoint:** `DELETE /divisiones/{id}`

**6. Obtener divisiones por turno**
- **Endpoint:** `GET /divisiones/turno/{turno}`

---

## 🚀 **Cómo Usar en Frontend**

### Ejemplo con Fetch API (JavaScript)

**1. Login:**
```javascript
const response = await fetch('http://localhost:8000/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    correoMatricula: 'admin@uteq.edu',
    password: 'admin123'
  })
});

const data = await response.json();
localStorage.setItem('token', data.token);
```

**2. Obtener lista de usuarios:**
```javascript
const token = localStorage.getItem('token');

const response = await fetch('http://localhost:8000/usuarios', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});

const usuarios = await response.json();
console.log(usuarios);
```

**3. Crear usuario:**
```javascript
const response = await fetch('http://localhost:8000/usuarios', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    correoMatricula: 'new@uteq.edu',
    password: 'pass123',
    nombre: 'Carlos',
    apellido: 'López',
    activo: true,
    rolId: 3
  })
});

const nuevoUsuario = await response.json();
```

---

## ⚠️ Códigos de Error Comunes

| Código | Significado | Descripción |
|--------|-----------|-------------|
| `200` | OK | Solicitud exitosa |
| `201` | Created | Recurso creado exitosamente |
| `204` | No Content | Solicitud exitosa, sin contenido |
| `400` | Bad Request | Datos inválidos o incompletos |
| `401` | Unauthorized | Token faltante o inválido |
| `403` | Forbidden | No tiene permisos |
| `404` | Not Found | Recurso no encontrado |
| `500` | Server Error | Error en el servidor |

---

## 🔑 Credenciales de Prueba

```
Email: admin@uteq.edu
Contraseña: admin123
Rol: ADMIN

Email: coordinador1@uteq.edu
Contraseña: pass123
Rol: COORDINADOR

Email: profesor1@uteq.edu
Contraseña: pass123
Rol: PROFESOR

Email: alumno1@uteq.edu
Contraseña: pass123
Rol: ALUMNO
```

---

## 📌 Notas Importantes

1. **Token JWT**: Se envía en el header `Authorization: Bearer <token>`
2. **CORS**: Ya está configurado en todos los servicios
3. **Content-Type**: Siempre debe ser `application/json`
4. **Fechas**: Formato `YYYY-MM-DD`
5. **Horas**: Formato `HH:mm` (24 horas)
6. **Valores booleanos**: `true` o `false` (minúsculas)

---

## 🌐 Puertos y URLs

| Servicio | Puerto | URL |
|----------|--------|-----|
| Eureka Server | 8761 | http://localhost:8761 |
| MS-Auth | 8088 | http://localhost:8088 |
| API Gateway | 8000 | http://localhost:8000 |
| MS-Admin | 8081 | http://localhost:8081 |
| MS-Asesorías | 8082 | http://localhost:8082 |
| MS-Coordinadores | 8083 | http://localhost:8083 |
| MS-Divisiones | 8084 | http://localhost:8084 |
| MS-Profesores | 8085 | http://localhost:8085 |
| MS-Alumnos | 8086 | http://localhost:8086 |

---

**Última actualización**: 25 de Noviembre de 2025
