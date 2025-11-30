# 🚀 GUÍA DE EJECUCIÓN - SISTEMA DE ASESORÍAS

## ¿QUÉ TENGO QUE HACER?

### OPCIÓN 1: La Más Fácil (RECOMENDADA)

**Solo 1 comando en PowerShell:**

```powershell
.\start-all.ps1
```

✅ Se abrirán 9 ventanas
✅ Compilarán automáticamente
✅ Se levantarán los servicios
✅ Verás todos los logs en vivo

---

### OPCIÓN 2: Hacerlo Manualmente

Si prefieres control total:

#### Paso 1: Abre 9 PowerShells

```powershell
# PowerShell 1
cd C:\Users\david\Downloads\asesorias-microservices-\eureka-server

# PowerShell 2
cd C:\Users\david\Downloads\asesorias-microservices-\ms-auth

# PowerShell 3
cd C:\Users\david\Downloads\asesorias-microservices-\api-gateway

# PowerShell 4
cd C:\Users\david\Downloads\asesorias-microservices-\ms-admin

# PowerShell 5
cd C:\Users\david\Downloads\asesorias-microservices-\ms-asesorias

# PowerShell 6
cd C:\Users\david\Downloads\asesorias-microservices-\ms-coordinadores

# PowerShell 7
cd C:\Users\david\Downloads\asesorias-microservices-\ms-divisiones

# PowerShell 8
cd C:\Users\david\Downloads\asesorias-microservices-\ms-profesores

# PowerShell 9
cd C:\Users\david\Downloads\asesorias-microservices-\ms-alumnos
```

#### Paso 2: Ejecuta en ESTE ORDEN

**Espera 10 segundos entre cada uno:**

```powershell
# 1️⃣ PRIMERO (en PowerShell 1)
mvn spring-boot:run

# 2️⃣ SEGUNDO (espera 10 seg, en PowerShell 2)
mvn spring-boot:run

# 3️⃣ TERCERO (espera 10 seg, en PowerShell 3)
mvn spring-boot:run

# Y así sucesivamente en orden...
```

---

## ✅ VERIFICAR QUE FUNCIONE

### Paso 1: Abre Eureka en navegador

```
http://localhost:8761
```

**Deberías ver 8 servicios con estado UP (verde)**

### Paso 2: Obtén un token

Abre Postman o terminal y ejecuta:

```
POST http://localhost:8000/api/auth/login

Body JSON:
{
  "correoMatricula": "admin@uteq.edu",
  "password": "admin123"
}
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

### Paso 3: Prueba un endpoint protegido

```
GET http://localhost:8000/api/divisiones

Header:
Authorization: Bearer {PEGA_EL_TOKEN_AQUÍ}
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "nombre": "1er Año",
    "descripcion": "Primer año de secundaria",
    "nivel": 1,
    "activo": true
  },
  ...
]
```

---

## 🔑 USUARIOS DE PRUEBA

```
✅ ADMIN
Email: admin@uteq.edu
Contraseña: admin123

✅ PROFESOR
Email: profesor1@uteq.edu
Contraseña: pass123

✅ COORDINADOR
Email: coordinador1@uteq.edu
Contraseña: pass123

✅ ALUMNO
Email: alumno1@uteq.edu
Contraseña: pass123
```

---

## 📍 TODOS LOS PUERTOS

| Servicio | Puerto | URL |
|----------|--------|-----|
| Eureka | 8761 | http://localhost:8761 |
| API Gateway | 8000 | http://localhost:8000 |
| MS-Auth | 8088 | http://localhost:8088 |
| MS-Admin | 8081 | http://localhost:8081 |
| MS-Asesorías | 8082 | http://localhost:8082 |
| MS-Coordinadores | 8083 | http://localhost:8083 |
| MS-Divisiones | 8084 | http://localhost:8084 |
| MS-Profesores | 8085 | http://localhost:8085 |
| MS-Alumnos | 8086 | http://localhost:8086 |

---

## ❌ PROBLEMAS?

### Problema: "mvn no se reconoce"
**Solución:** Maven no está en el PATH
- Instala Maven nuevamente
- O usa Visual Studio que tiene Maven integrado

### Problema: "Connection refused"
**Solución:** Un servicio no está levantado
- Verifica Eureka en http://localhost:8761
- Espera 10 segundos más antes de iniciar el siguiente

### Problema: "Puerto ya en uso"
**Solución:** 
```powershell
# Busca qué está usando el puerto 8082
netstat -ano | findstr :8082

# Termina ese proceso
taskkill /PID {numero} /F
```

### Problema: "Eureka muestra servicios en RED"
**Solución:** 
- Son los health checks del gateway
- Espera 30 segundos, deberían ponerse en verde

---

## 🛑 PARAR TODO

```
Cierra todas las ventanas PowerShell
O presiona Ctrl+C en cada una
```

---

## 🔄 REINICIAR UN SERVICIO

```powershell
# En la ventana del servicio

# 1. Presiona Ctrl+C para detener
Ctrl+C

# 2. Ejecuta nuevamente
mvn clean spring-boot:run
```

---

## 📊 ENDPOINTS PRINCIPALES

### MS-ADMIN
```
GET    /api/usuarios              - Listar usuarios
POST   /api/usuarios              - Crear usuario
GET    /api/divisiones            - Listar divisiones
POST   /api/divisiones            - Crear división
GET    /api/programas             - Listar programas
POST   /api/programas             - Crear programa
GET    /api/grupos                - Listar grupos
POST   /api/grupos                - Crear grupo
```

### MS-ASESORÍAS
```
GET    /api/asesorias             - Listar asesorías
POST   /api/asesorias             - Crear asesoría
GET    /api/disponibilidades      - Listar disponibilidades
POST   /api/disponibilidades      - Crear disponibilidad
```

### MS-COORDINADORES
```
PUT    /api/coordinadores/asignar/profesor/{id}
PUT    /api/coordinadores/asignar/alumno/{id}
GET    /api/coordinadores/profesores
GET    /api/coordinadores/alumnos
```

### MS-DIVISIONES
```
GET    /api/divisiones            - Listar
POST   /api/divisiones            - Crear
GET    /api/programas             - Listar
POST   /api/programas             - Crear
```

### MS-PROFESORES
```
GET    /api/profesores            - Listar
POST   /api/profesores            - Crear
```

### MS-ALUMNOS
```
GET    /api/alumnos               - Listar
POST   /api/alumnos               - Crear
```

---

## 💡 TIPS ÚTILES

### Postman collection

Importa esta URL en Postman:
```
[NO HAY URL - CRÉALO TÚ O USA CURL/THUNDER CLIENT]
```

### Ver logs de un servicio

Cada ventana PowerShell muestra los logs en vivo:
- Busca "ERROR" para errores
- Busca "INFO" para información
- Busca el puerto para verificar que inició

### Cambiar puerto de un servicio

En `application.yml` del servicio:
```yaml
server:
  port: 8082  # Cambia aquí
```

### Usar base de datos PostgreSQL en lugar de H2

En `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/asesorias
    username: postgres
    password: password
```

---

## 🎓 PRÓXIMOS PASOS

1. ✅ Ejecuta `.\start-all.ps1`
2. ✅ Espera a que terminen de compilar
3. ✅ Abre http://localhost:8761
4. ✅ Verifica que todos estén UP (verde)
5. ✅ Obtén token con login
6. ✅ Prueba los endpoints
7. ✅ Crea tu frontend que consuma la API

---

## 📞 REFERENCIAS RÁPIDAS

**Documentos completos:**
- `INICIO_RAPIDO.md` - Guía rápida (2 pasos)
- `AUDITORIA_FINAL.md` - Auditoría técnica completa
- `IMPLEMENTACION_FINAL.md` - Detalles de implementación

**Ver logs de compilación:**
- Cada ventana PowerShell muestra los logs
- Busca "BUILD SUCCESS" para verificar que compiló bien
- Busca "Tomcat started" para verificar que se levantó

**Testear sin frontend:**
- Usa Postman
- Usa Thunder Client (extensión VS Code)
- Usa curl en terminal

---

## ✨ RESUMEN

**3 formas de empezar:**

1. **Lo más fácil:** `.\start-all.ps1`
2. **Con control:** Abre 9 PowerShells y ejecuta mvn en cada una
3. **Desde Visual Studio:** Usa el panel de Maven

**Resultado:**
- ✅ 9 microservicios corriendo
- ✅ Autenticación JWT funcionando
- ✅ API Gateway validando tokens
- ✅ Todo listo para que consumas desde tu frontend

---

**¡LISTO! Ya puedes empezar a trabajar** 🚀

Si tienes dudas, consulta los otros documentos o los logs en PowerShell.
