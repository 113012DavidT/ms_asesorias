# 📮 Guía de Postman - Sistema de Asesorías

## 🚀 Instalación y Setup

### 1. Descargar Postman
- Descarga desde: https://www.postman.com/downloads/
- O usa la versión web: https://web.postman.co/

### 2. Importar la Colección

**Opción A - Desde archivo JSON:**
1. Abre Postman
2. Click en **"File"** → **"Import"**
3. Selecciona el archivo `Postman_Collection.json`
4. La colección se cargará automáticamente

**Opción B - Copiar desde URL:**
1. Abre Postman
2. Click en **"Import"** en la esquina superior izquierda
3. Selecciona la pestaña **"Raw text"**
4. Pega el contenido del archivo JSON
5. Click en **"Import"**

### 3. Configurar Variable de Token

El archivo incluye una variable `{{token}}` que usarás después de login:

1. En Postman, ve a la pestaña **"Variables"**
2. Busca la variable `token`
3. Después de hacer login, copia el token del response
4. Pégalo en el campo **"Current value"** de la variable `token`

---

## 🧪 Plan de Pruebas Recomendado

### Fase 1: Autenticación ✅

**Ejecuta en orden:**

1. **Login - ADMIN**
   - Endpoint: `POST /auth/login`
   - Guarda el `token` del response
   - Actualiza la variable `{{token}}` en Postman

2. **Login - PROFESOR**
   - Prueba con diferentes credenciales
   - Verifica que obtiene token correctamente

3. **Login - ALUMNO**
   - Verifica acceso como estudiante

---

### Fase 2: Gestión Administrativa 📋

**Usuarios:**
1. Listar usuarios
2. Obtener usuario por ID (ID: 1)
3. Crear usuario (nuevo email)
4. Actualizar usuario (cambiar nombre)
5. Eliminar usuario

**Roles:**
1. Listar roles
2. Obtener rol por ID (ID: 1)

**Divisiones:**
1. Listar divisiones
2. Obtener división por ID (ID: 1)
3. Crear división (nuevo nombre)
4. Actualizar división
5. Eliminar división

**Programas:**
1. Listar programas
2. Obtener programa por ID (ID: 1)
3. Crear programa (Derecho)

---

### Fase 3: Gestión de Personas 👥

**Profesores:**
1. Listar profesores
2. Obtener profesor por ID (ID: 1)
3. Profesores por división (division: 1)
4. Profesores por programa (programa: 1)

**Alumnos:**
1. Listar alumnos
2. Obtener alumno por ID (ID: 1)
3. Alumnos por división (division: 1)
4. Alumnos por programa (programa: 1)
5. Actualizar promedio (cambiar a 9.5)

**Coordinadores:**
1. Listar coordinadores
2. Obtener coordinador por ID (ID: 1)
3. Coordinadores por división (division: 1)

---

### Fase 4: Asesorías 📚

**Operaciones:**
1. Listar asesorías
2. Obtener asesoría por ID (ID: 1)
3. Crear asesoría (nueva)
4. Actualizar asesoría (estado: COMPLETADA)
5. Asesorías por profesor (profesor: 1)
6. Asesorías por alumno (alumno: 1)

**Valores para crear asesoría:**
```json
{
  "titulo": "Asesoría de Programación",
  "descripcion": "Tutoría de Java Avanzado",
  "profesorId": 1,
  "alumnoId": 1,
  "grupoId": 1,
  "fecha": "2025-12-01",
  "horaInicio": "14:00",
  "horaFin": "15:00"
}
```

---

### Fase 5: Grupos y Perfiles 🔗

**Grupos:**
1. Listar grupos
2. Obtener grupo por ID (ID: 1)
3. Crear grupo (nuevo)

**Perfiles:**
1. Listar perfiles coordinador
2. Listar perfiles profesor
3. Listar perfiles alumno

---

## 💡 Tips y Trucos

### 1. Usar el Pre-request Script (Automatizar Token)

Si quieres automatizar la extracción del token:

1. Ve a cualquier endpoint con token
2. Abre la pestaña **"Pre-request Script"**
3. Pega este código:

```javascript
// Solo necesitas ejecutar esto después de login ADMIN
// El token se guardará automáticamente en la variable
```

### 2. Guardar Respuestas en Variables

Después de hacer login:

1. Abre la pestaña **"Tests"** en el request de login
2. Pega este código:

```javascript
var jsonData = pm.response.json();
pm.variables.set("token", jsonData.token);
```

3. Ejecuta el request - el token se guardará automáticamente

### 3. Ver Respuesta Formateada

1. Ejecuta un request
2. En la sección **"Response"**, selecciona la vista **"Pretty"**
3. Verás el JSON formateado y fácil de leer

### 4. Validar Respuestas con Tests

En la pestaña **"Tests"** puedes agregar validaciones:

```javascript
// Verificar que la respuesta es 200
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Verificar que existe un campo
pm.test("Response has id", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
});
```

---

## 🔑 Credenciales Disponibles

```
ADMIN:
  Email: admin@uteq.edu
  Contraseña: admin123

PROFESOR:
  Email: profesor1@uteq.edu
  Contraseña: pass123

ALUMNO:
  Email: alumno1@uteq.edu
  Contraseña: pass123

COORDINADOR:
  Email: coordinador1@uteq.edu
  Contraseña: pass123
```

---

## 📊 Escenarios de Prueba Avanzados

### Escenario 1: Flujo Completo de Asesoría

1. **Login como PROFESOR**
   - Obtén token

2. **Crear asesoría**
   - Endpoint: `POST /asesorias`
   - Body:
   ```json
   {
     "titulo": "Tutoría Completa",
     "descripcion": "Sesión de tutoría completa",
     "profesorId": 1,
     "alumnoId": 1,
     "grupoId": 1,
     "fecha": "2025-12-05",
     "horaInicio": "10:00",
     "horaFin": "11:00"
   }
   ```

3. **Obtener asesoría creada**
   - Copia el ID del response anterior
   - Endpoint: `GET /asesorias/{id}`

4. **Actualizar estado**
   - Endpoint: `PUT /asesorias/{id}`
   - Body:
   ```json
   {
     "estado": "COMPLETADA"
   }
   ```

### Escenario 2: Gestión de Usuarios

1. **Crear nuevo usuario**
   - Rol: PROFESOR (rolId: 3)
   
2. **Buscar el usuario creado**
   - Listar todos y verificar

3. **Actualizar información**
   - Cambiar nombre/apellido

4. **Crear perfil de profesor**
   - Endpoint: `POST /profesor-perfiles`

### Escenario 3: Reportes

1. **Profesores por programa**
   - `GET /profesores/programa/1`

2. **Alumnos por división**
   - `GET /alumnos/division/1`

3. **Asesorías por profesor**
   - `GET /asesorias/profesor/1`

---

## ⚠️ Errores Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| `401 Unauthorized` | Token inválido o expirado | Haz login nuevamente |
| `404 Not Found` | ID no existe | Verifica el ID correcto |
| `400 Bad Request` | Datos incompletos | Revisa que todos los campos requeridos estén |
| `500 Server Error` | Error en servidor | Revisa los logs del servicio |

---

## 🎯 Checklist de Pruebas Completas

- [ ] Login exitoso como ADMIN
- [ ] Token guardado correctamente
- [ ] Listar usuarios (GET)
- [ ] Crear usuario (POST)
- [ ] Actualizar usuario (PUT)
- [ ] Obtener usuario por ID (GET)
- [ ] Eliminar usuario (DELETE)
- [ ] Listar roles
- [ ] Listar divisiones
- [ ] Crear división
- [ ] Listar programas
- [ ] Listar profesores
- [ ] Listar alumnos
- [ ] Actualizar promedio de alumno
- [ ] Listar coordinadores
- [ ] Listar asesorías
- [ ] Crear asesoría
- [ ] Actualizar asesoría
- [ ] Filtrar asesorías por profesor
- [ ] Filtrar asesorías por alumno
- [ ] Listar grupos
- [ ] Listar perfiles

---

## 📱 Exportar Colección Personalizada

Si quieres crear tu propia colección:

1. En Postman, click derecho en la carpeta
2. **"Export"**
3. Selecciona formato **"Collection v2.1"**
4. Guarda como `.json`

---

## 🔗 URLs de Referencia

| Servicio | URL |
|----------|-----|
| API Gateway | http://localhost:8000 |
| MS-Auth | http://localhost:8088 |
| MS-Admin | http://localhost:8081 |
| MS-Asesorías | http://localhost:8082 |
| Eureka | http://localhost:8761 |

---

## 📞 Soporte

Si encuentras errores:

1. Verifica que los servicios estén corriendo: `docker-compose ps`
2. Revisa los logs: `docker logs <servicio>`
3. Asegúrate de tener el token válido
4. Verifica que uses la URL correcta (http://localhost:8000)

---

**Creado**: 25 de Noviembre de 2025
