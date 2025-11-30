# 📋 INSTRUCCIONES PARA EJECUTAR EL SISTEMA

## Opción 1: Ejecutar desde Visual Studio (Recomendado para desarrollo)

### Paso 1: Compilar con Maven desde Visual Studio

1. Abre Visual Studio y carga el proyecto raíz
2. En la Terminal integrada (o Maven Panel), ejecuta:

```bash
cd eureka-server
mvn clean install -DskipTests

cd ../ms-auth
mvn clean install -DskipTests

cd ../api-gateway
mvn clean install -DskipTests

cd ../ms-admin
mvn clean install -DskipTests

cd ../ms-asesorias
mvn clean install -DskipTests

cd ../ms-coordinadores
mvn clean install -DskipTests

cd ../ms-divisiones
mvn clean install -DskipTests

cd ../ms-profesores
mvn clean install -DskipTests

cd ../ms-alumnos
mvn clean install -DskipTests
```

### Paso 2: Ejecutar cada microservicio

**Opción A: Desde Visual Studio (Maven Panel)**

En Maven Panel de cada proyecto:
- Haz clic derecho en `spring-boot:run`
- O ejecuta: `mvn spring-boot:run`

**Orden de ejecución:**
1. eureka-server (puerto 8761)
2. ms-auth (puerto 8088)
3. api-gateway (puerto 8000)
4. ms-admin (puerto 8081)
5. ms-asesorias (puerto 8082)
6. ms-coordinadores (puerto 8083)
7. ms-divisiones (puerto 8084)
8. ms-profesores (puerto 8085)
9. ms-alumnos (puerto 8086)

---

## Opción 2: Usar Docker Compose (Si tienes imágenes JAR pre-compiladas)

### Prerequisitos:
- Compilar primero los JARs con Maven (ver Opción 1)
- Los JARs deben estar en: `./target/` de cada microservicio

### Comando:
```bash
cd c:\Users\david\Downloads\asesorias-microservices-

# Construir imágenes
docker-compose build

# Iniciar contenedores
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar servicios
docker-compose down
```

---

## URLs y Puertos Importantes

| Servicio | URL | Puerto |
|----------|-----|--------|
| Eureka Dashboard | http://localhost:8761 | 8761 |
| API Gateway | http://localhost:8000 | 8000 |
| MS-Auth | http://localhost:8088 | 8088 |
| MS-Admin | http://localhost:8081 | 8081 |
| MS-Asesorías | http://localhost:8082 | 8082 |
| MS-Coordinadores | http://localhost:8083 | 8083 |
| MS-Divisiones | http://localhost:8084 | 8084 |
| MS-Profesores | http://localhost:8085 | 8085 |
| MS-Alumnos | http://localhost:8086 | 8086 |

---

## Testing de Autenticación

### 1. Login para obtener token

```bash
curl -X POST "http://localhost:8000/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "admin@uteq.edu",
    "password": "admin123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "usuarioId": 1,
  "rolNombre": "ADMIN"
}
```

### 2. Usar token en requests

```bash
curl -X GET "http://localhost:8000/api/divisiones" \
  -H "Authorization: Bearer {TOKEN_AQUI}"
```

### 3. Validar token

```bash
curl -X POST "http://localhost:8000/api/auth/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "token": "{TOKEN_AQUI}"
  }'
```

---

## Usuarios de Prueba (en ms-admin)

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@uteq.edu | admin123 | ADMIN |
| coordinador1@uteq.edu | pass123 | COORDINADOR |
| profesor1@uteq.edu | pass123 | PROFESOR |
| alumno1@uteq.edu | pass123 | ALUMNO |

---

## Verificar Estado del Sistema

### En Visual Studio:

1. Abre http://localhost:8761 (Eureka Dashboard)
2. Verifica que todos los servicios aparezcan como "UP" (verde)

### Con curl:

```bash
# Verificar Eureka
curl http://localhost:8761

# Verificar API Gateway
curl http://localhost:8000/actuator/health

# Verificar MS específico
curl http://localhost:8081/actuator/health
```

---

## Troubleshooting

### Error: "Connection refused"
- Verifica que los servicios estén corriendo
- Comprueba los puertos no estén siendo usados por otro proceso
- Reinicia Visual Studio

### Error: "Eureka not available"
- Asegúrate que eureka-server esté corriendo primero
- Espera 10-15 segundos para que se registren los servicios

### Error: "Cannot resolve ms-admin"
- Los Feign clients usan URLs localhost
- En Docker Compose, usan nombres de servicios
- Revisa el Dockerfile si estás migrando entre dev y docker

### Error de compilación Maven
- Asegúrate de tener Maven 3.9+ instalado
- Verifica que Java 17+ esté en el PATH
- Limpia caché: `mvn clean`

---

## Próximas Acciones

1. ✅ Compilar todos los servicios con Maven
2. ✅ Ejecutar servicios en orden desde Visual Studio
3. ✅ Verificar en Eureka que aparezcan todos
4. ✅ Probar login y obtener token
5. ✅ Testear endpoints con token
6. 📊 Monitorear logs en cada servicio

---

## Documentación Relacionada

- `AUDITORIA_FINAL.md` - Auditoría completa del sistema
- `IMPLEMENTACION_FINAL.md` - Detalles de implementación
- `MS-AUTH-DOCUMENTACION.md` - Documentación de autenticación
- `ARQUITECTURA_MICROSERVICIOS_COMPLETA.md` - Arquitectura general

---

**Última actualización:** 25 de Noviembre de 2025
**Version:** 2.0.0
