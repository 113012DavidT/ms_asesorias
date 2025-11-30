# 🚀 GUÍA DE IMPLEMENTACIÓN Y EJECUCIÓN

## Requisitos Previos

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- Git

## Instalación Local (Sin Docker)

### 1. Compilar Proyectos

```bash
# Compilar todos los servicios
mvn clean install -DskipTests

# O por individual
cd ms-admin
mvn clean install -DskipTests
cd ../eureka-server
mvn clean install -DskipTests
# ... repetir para otros servicios
```

### 2. Ejecutar Servicios en Orden

```bash
# Terminal 1: Eureka Server
cd eureka-server
mvn spring-boot:run

# Terminal 2: MS-Admin (esperar a que Eureka esté listo)
cd ms-admin
mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 4+: Otros microservicios
cd ms-asesorias
mvn spring-boot:run
```

## Ejecución con Docker Compose

### 1. Construir Imágenes

```bash
# Construir todas las imágenes
docker-compose build

# O construir una específica
docker-compose build ms-admin
```

### 2. Iniciar Servicios

```bash
# Iniciar todos los servicios
docker-compose up

# Iniciar en segundo plano
docker-compose up -d

# Iniciar servicios específicos
docker-compose up eureka-server api-gateway ms-admin
```

### 3. Detener Servicios

```bash
# Detener todos
docker-compose down

# Detener y limpiar volúmenes
docker-compose down -v
```

### 4. Ver Logs

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f ms-admin

# Ver últimas 100 líneas
docker-compose logs --tail=100 ms-admin
```

## URLs Importantes (Local)

| Servicio | URL | Puerto | Descripción |
|----------|-----|--------|-------------|
| **API Gateway** | http://localhost:8000 | 8000 | ⭐ USAR PARA FRONTEND |
| Eureka Dashboard | http://localhost:8761 | 8761 | Ver servicios registrados |
| MS-Auth (directo) | http://localhost:8088 | 8088 | Solo para testing |
| MS-Admin (directo) | http://localhost:8081 | 8081 | Solo para testing |
| MS-Asesorías (directo) | http://localhost:8082 | 8082 | Solo para testing |
| MS-Coordinadores (directo) | http://localhost:8083 | 8083 | Solo para testing |
| MS-Divisiones (directo) | http://localhost:8084 | 8084 | Solo para testing |
| MS-Profesores (directo) | http://localhost:8085 | 8085 | Solo para testing |
| MS-Alumnos (directo) | http://localhost:8086 | 8086 | Solo para testing |
| H2 Console (MS-Admin) | http://localhost:8081/h2-console | - | Base de datos |

---

⚠️ **IMPORTANTE PARA EL FRONTEND:**
- **Todas las llamadas deben ir al API Gateway:** `http://localhost:8000`
- **NO acceder directamente a los microservicios** en puertos 8081, 8082, etc.
- **Endpoint de login:** `POST http://localhost:8000/api/auth/login`
- **Todas las requests necesitan el JWT token en Authorization header:** `Authorization: Bearer {token}`

## Credenciales por Defecto (H2 Console)

- Driver Class: org.h2.Driver
- JDBC URL: jdbc:h2:mem:admin_db
- Usuario: sa
- Contraseña: (dejar en blanco)

## Datos Precargados en MS-Admin

Al iniciar ms-admin, automáticamente se crean:

### Roles
- ADMIN
- COORDINADOR
- PROFESOR
- ALUMNO

### Usuarios de Ejemplo
- **Admin**: admin@uteq.edu / admin123
- **Coordinador 1**: coordinador1@uteq.edu / pass123
- **Profesor 1**: profesor1@uteq.edu / pass123
- **Alumno 1**: alumno1@uteq.edu / pass123

### Divisiones
- 1ro A, 1ro B, 2do A, 2do B

### Programas
- Ingeniería en Sistemas
- Administración de Empresas
- Contabilidad

### Grupos de Ejemplo
- Grupo 1 - Matemáticas
- Grupo 2 - Programación
- Grupo 3 - Física
- Grupo 1 - Finanzas

## 🔐 ENDPOINTS DE AUTENTICACIÓN (MS-AUTH)

**Base URL:** `http://localhost:8000/api/auth`

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "correoMatricula": "admin@uteq.edu",
  "password": "admin123"
}

# Response (200 OK)
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWI...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWI...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Usuario Prueba",
  "rolNombre": "ADMIN",
  "expiresIn": 86400
}
```

### Validar Token
```bash
POST /api/auth/validate
Content-Type: application/json
Authorization: Bearer {token}

{
  "token": "{JWT_TOKEN}"
}

# Response (200 OK)
{
  "valid": true,
  "usuarioId": 1,
  "correoMatricula": "admin@uteq.edu",
  "rolNombre": "ADMIN",
  "mensaje": "Token válido"
}
```

### Refrescar Token
```bash
POST /api/auth/refresh
Authorization: Bearer {refreshToken}

# Response (200 OK)
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWI...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWI...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Usuario Prueba",
  "rolNombre": "ADMIN"
}
```

### Health Check
```bash
GET /api/auth/health

# Response (200 OK)
"MS-Auth está funcionando correctamente"
```

---

## 📊 ENDPOINTS POR MICROSERVICIO (A través del API Gateway)

### MS-Admin
**Base URL:** `http://localhost:8000/api/admin`
- `GET /api/admin/usuarios` - Listar usuarios
- `POST /api/admin/usuarios` - Crear usuario
- `GET /api/admin/roles` - Listar roles
- `POST /api/admin/roles` - Crear rol

### MS-Divisiones
**Base URL:** `http://localhost:8000/api/divisiones`
- `GET /api/divisiones` - Listar divisiones
- `POST /api/divisiones` - Crear división
- `GET /api/programas` - Listar programas
- `POST /api/programas` - Crear programa

### MS-Asesorías
**Base URL:** `http://localhost:8000/api/asesorias`
- `GET /api/asesorias` - Listar asesorías
- `POST /api/asesorias` - Crear asesoría
- `GET /api/disponibilidades` - Listar disponibilidades
- `POST /api/disponibilidades` - Crear disponibilidad

### MS-Profesores
**Base URL:** `http://localhost:8000/api/profesores`
- `GET /api/profesores` - Listar profesores
- `POST /api/profesores` - Crear profesor
- `GET /api/profesores/{id}` - Obtener profesor

### MS-Alumnos
**Base URL:** `http://localhost:8000/api/alumnos`
- `GET /api/alumnos` - Listar alumnos
- `POST /api/alumnos` - Crear alumno
- `GET /api/alumnos/{id}` - Obtener alumno

### MS-Coordinadores
**Base URL:** `http://localhost:8000/api/coordinadores`
- `GET /api/coordinadores` - Listar coordinadores
- `POST /api/coordinadores` - Crear coordinador

---

## Pruebas de API

### Con cURL

```bash
# ✅ Login (CORRECTO - A través del Gateway)
curl -X POST "http://localhost:8000/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "admin@uteq.edu",
    "password": "admin123"
  }'

# ❌ NO USAR - Endpoint obsoleto
# curl -X POST "http://localhost:8081/api/admin/usuarios/login?correo=admin@uteq.edu&password=admin123"

# Listar usuarios (requiere token)
curl -X GET "http://localhost:8000/api/admin/usuarios" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Listar divisiones
curl -X GET "http://localhost:8000/api/divisiones" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Crear nueva división
curl -X POST "http://localhost:8000/api/divisiones" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "nombre": "3ro A",
    "descripcion": "Tercer año paralela A",
    "activo": true
  }'
```

### Con Postman

1. Importar colección desde `GUIA_POSTMAN.md`
2. Configurar variable de entorno: `BASE_URL=http://localhost:8000`
3. Ejecutar primero: **Login** para obtener token
4. Usar el token en las siguientes requests en `Authorization: Bearer {token}`

### Con Swagger/OpenAPI (Próximo paso)

```bash
# Agregar dependencia
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.0.0</version>
</dependency>

# URL: http://localhost:8000/swagger-ui.html (Gateway)
```

## Verificar Salud de Servicios

```bash
# Eureka - Ver aplicaciones registradas
curl http://localhost:8761/eureka/apps

# MS-Admin Health
curl http://localhost:8081/actuator/health
```

## Problemas Comunes

### 1. Puerto ya en uso
```bash
# Encontrar proceso en puerto 8081
lsof -i :8081
kill -9 <PID>

# O cambiar puerto en application.yml
server:
  port: 8099
```

### 2. Eureka no registra servicios
- Esperar 30 segundos (intervalo de heartbeat)
- Verificar que EUREKA_CLIENT_ENABLED esté true
- Revisar logs de Eureka

### 3. H2 Console no abre
- Verificar que eureka-client está habilitado
- Acceder directamente: http://localhost:8081/h2-console

### 4. Error al construir Docker
```bash
# Limpiar imágenes
docker-compose down --rmi all

# Reconstruir
docker-compose build --no-cache
```

## Flujo de Desarrollo

1. **Hacer cambios** en el código
2. **Recompilar**: `mvn clean package`
3. **Reiniciar servicio**: `docker-compose restart ms-admin`
4. **O sin Docker**: `mvn spring-boot:run`

## Próximos Pasos

1. Implementar MS-Auth con JWT
2. Agregar seguridad en API Gateway
3. Crear sincronización de datos entre servicios
4. Agregar tests unitarios e integración
5. Configurar CI/CD con GitHub Actions
6. Desplegar a Kubernetes

## Monitoreo en Producción

```bash
# Ver logs en tiempo real
docker-compose logs -f

# Estadísticas de contenedores
docker stats

# Inspeccionar contenedor
docker inspect ms-admin

# Ejecutar comando en contenedor
docker exec -it ms-admin bash
```

## Base de Datos - Cambio a PostgreSQL

Para cambiar de H2 a PostgreSQL en producción:

1. Agregar dependencia:
```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.7.0</version>
</dependency>
```

2. Actualizar application.yml:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/admin_db
    username: admin
    password: ${DB_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
```

3. Agregar servicio PostgreSQL en docker-compose.yml:
```yaml
postgres:
  image: postgres:15-alpine
  environment:
    POSTGRES_DB: admin_db
    POSTGRES_USER: admin
    POSTGRES_PASSWORD: password
  ports:
    - "5432:5432"
  volumes:
    - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

---

**Creado**: 2025-01-15
**Última actualización**: 2025-01-15
**Versión**: 1.0.0
