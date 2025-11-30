# MS-AUTH - Microservicio de Autenticación

## 📋 Descripción

Microservicio especializado en autenticación y autorización del sistema. Genera y valida tokens JWT para proteger todos los endpoints de la aplicación.

## 🏗️ Arquitectura

```
┌──────────────────────┐
│   Cliente (WEB/APP)  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────────────────┐
│       API GATEWAY (8000)          │ ◄── Verifica JWT aquí
└──────────┬───────────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│    MS-AUTH (8088)                │
│  - Generación de Tokens JWT      │
│  - Validación de Tokens          │
│  - Refresh Token                 │
└──────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│    MS-Admin (8081)               │
│  (Valida credenciales)           │
└──────────────────────────────────┘
```

## 🔐 Flujo de Autenticación

### 1. Login (Obtener Token)
```
POST /api/auth/login
{
  "correoMatricula": "usuario@uteq.edu",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "rolNombre": "ADMIN",
  "expiresIn": 86400
}
```

### 2. Usar Token en Requests
```
GET /api/divisiones
Authorization: Bearer eyJhbGc...
```

### 3. Validar Token
```
POST /api/auth/validate
{
  "token": "eyJhbGc..."
}

Response:
{
  "valid": true,
  "usuarioId": 1,
  "correoMatricula": "usuario@uteq.edu",
  "rolNombre": "ADMIN",
  "mensaje": "Token válido"
}
```

### 4. Refrescar Token
```
POST /api/auth/refresh
Authorization: Bearer eyJhbGc... (refresh token)

Response:
{
  "token": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  ...
}
```

## 📊 Estructura JWT

### Token JWT (Access Token)
```
Header: {
  "alg": "HS512",
  "typ": "JWT"
}

Payload: {
  "sub": "1",                                  // Usuario ID
  "correoMatricula": "usuario@uteq.edu",
  "rol": "ADMIN",
  "iat": 1705334400,                          // Issued at
  "exp": 1705420800                           // Expires in 24 hours
}

Signature: HMAC512(secret)
```

## 🚀 Endpoints

### Login
```
POST /api/auth/login
Content-Type: application/json

Body:
{
  "correoMatricula": "admin@uteq.edu",
  "password": "admin123"
}

Response: 200 OK
{
  "token": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "type": "Bearer",
  "usuarioId": 1,
  "nombre": "Juan",
  "rolNombre": "ADMIN",
  "expiresIn": 86400
}
```

### Validar Token
```
POST /api/auth/validate
Content-Type: application/json

Body:
{
  "token": "eyJhbGc..."
}

Response: 200 OK
{
  "valid": true,
  "usuarioId": 1,
  "correoMatricula": "admin@uteq.edu",
  "rolNombre": "ADMIN",
  "mensaje": "Token válido"
}
```

### Refrescar Token
```
POST /api/auth/refresh
Authorization: Bearer eyJhbGc... (refresh token)

Response: 200 OK
{
  "token": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "type": "Bearer",
  "usuarioId": 1,
  "correoMatricula": "admin@uteq.edu",
  "rolNombre": "ADMIN"
}
```

### Health Check
```
GET /api/auth/health

Response: 200 OK
"MS-Auth está funcionando correctamente"
```

## ⚙️ Configuración

### application.yml
```yaml
server:
  port: 8088

spring:
  application:
    name: ms-auth

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

app:
  jwt:
    secret: my_super_secret_key...
    expiration: 86400000      # 24 horas en ms
    refresh-expiration: 604800000  # 7 días en ms
```

### Variables de Entorno (Docker)
```bash
EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=http://eureka-server:8761/eureka/
SPRING_APPLICATION_NAME=ms-auth
APP_JWT_SECRET=your_secret_key_here
```

## 🔑 Configuración de Secreto JWT (Producción)

**IMPORTANTE**: Cambiar el secreto en producción

```bash
# Generar secreto seguro
openssl rand -base64 32

# Configurar en environment variables
export APP_JWT_SECRET="tu_secreto_seguro_generado"
```

## 🔒 Integración con API Gateway

El API Gateway valida tokens automáticamente:

### Rutas Públicas (Sin Autenticación)
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
... todas las demás rutas
```

### Headers Agregados por Gateway
```
X-User-Id: 1
X-User-Email: usuario@uteq.edu
X-User-Role: ADMIN
```

## 📦 Dependencias

```xml
<!-- Spring Security -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.12.3</version>
</dependency>
```

## 🧪 Pruebas

### Con cURL

**Login**
```bash
curl -X POST "http://localhost:8088/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "admin@uteq.edu",
    "password": "admin123"
  }'
```

**Validar Token**
```bash
curl -X POST "http://localhost:8088/api/auth/validate" \
  -H "Content-Type: application/json" \
  -d '{"token": "eyJhbGc..."}'
```

**Usar Token**
```bash
curl -X GET "http://localhost:8081/api/divisiones" \
  -H "Authorization: Bearer eyJhbGc..."
```

### Con Postman
1. Importar: `MS-Auth-Postman-Collection.json`
2. Usar variable: `{{TOKEN}}`
3. Obtener token del login y establecer variable

## 🐳 Docker

**Construir imagen**
```bash
docker build -t asesorias/ms-auth:1.0.0 .
```

**Ejecutar contenedor**
```bash
docker run -d \
  -p 8088:8088 \
  -e EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=http://eureka-server:8761/eureka/ \
  -e APP_JWT_SECRET="your_secret" \
  --network asesorias-network \
  asesorias/ms-auth:1.0.0
```

**Con docker-compose**
```bash
docker-compose up ms-auth
```

## 🔗 Integración con Otros Microservicios

Cada microservicio puede:

1. **Validar tokens localmente** (copiar JwtService)
```java
@Component
public class JwtValidator {
    private final JwtService jwtService;
    
    public boolean esValido(String token) {
        return jwtService.validarToken(token);
    }
}
```

2. **Validar tokens remotamente** (llamar a ms-auth)
```java
@FeignClient(name = "ms-auth")
public interface AuthClient {
    @PostMapping("/api/auth/validate")
    TokenValidationResponse validarToken(@RequestBody TokenValidationRequest request);
}
```

## ⚠️ Seguridad

### Buenas Prácticas
- ✅ Usar HTTPS en producción
- ✅ Cambiar secreto JWT en producción
- ✅ Usar CORS restringido
- ✅ Rate limiting en login
- ✅ Validar tokens en API Gateway
- ✅ No exponer secreto en logs
- ✅ Implementar logout (blacklist de tokens)
- ✅ Usar refresh tokens

### No Hacer
- ❌ Almacenar contraseña en texto plano
- ❌ Usar secreto débil o por defecto
- ❌ Exponer token en URL
- ❌ Tokens sin expiración

## 🚨 Errores Comunes

### 401 Unauthorized - Token Faltante
```
Error: "Token no proporcionado"
Solución: Agregar header Authorization
```

### 401 Unauthorized - Token Expirado
```
Error: "Token inválido o expirado"
Solución: Usar refresh token para obtener nuevo token
```

### 401 Unauthorized - Token Malformado
```
Error: "Token JWT inválido"
Solución: Verificar formato: "Bearer <token>"
```

## 📚 Próximas Mejoras

- [ ] Blacklist de tokens para logout
- [ ] 2FA (Two-Factor Authentication)
- [ ] OAuth2 integration
- [ ] OpenID Connect
- [ ] Tokens con scope granular
- [ ] Rate limiting
- [ ] Audit logging
- [ ] Token revocation
- [ ] Multi-tenant support

## 📄 Notas

- **Secreto JWT**: Cambiar en producción
- **Expiración**: 24 horas (configurable)
- **Refresh Expiration**: 7 días (configurable)
- **Algoritmo**: HS512
- **Encoding**: Base64URL

---

**Versión**: 1.0.0
**Última actualización**: Enero 2025
**Estado**: ✅ Producción Ready
