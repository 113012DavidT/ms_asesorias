# 🚀 GUÍA RÁPIDA DE INICIO

## ✅ REQUISITOS

- [x] Visual Studio o IDE con soporte Maven
- [x] Java 17+
- [x] Maven 3.9+
- [x] Docker Desktop (opcional, para ejecutar en contenedores)

---

## 🎯 OPCIÓN 1: Ejecutar TODO de una vez (RECOMENDADO)

### Paso 1: Ejecutar Script de Inicio

**Abre PowerShell en la carpeta del proyecto y ejecuta:**

```powershell
.\start-all.ps1
```

**¿Qué hace?**
- Abre 9 ventanas PowerShell separadas
- Compila Maven en cada una
- Inicia los servicios en orden
- Cada ventana muestra sus logs en tiempo real

**Resultado:**
```
✅ Eureka Server        → http://localhost:8761
✅ MS-Auth             → http://localhost:8088
✅ API Gateway         → http://localhost:8000
✅ MS-Admin            → http://localhost:8081
✅ MS-Asesorías        → http://localhost:8082
✅ MS-Coordinadores    → http://localhost:8083
✅ MS-Divisiones       → http://localhost:8084
✅ MS-Profesores       → http://localhost:8085
✅ MS-Alumnos          → http://localhost:8086
```

---

## 🎯 OPCIÓN 2: Ejecutar Manualmente desde Visual Studio

### Paso 1: Abrir cada carpeta en una terminal separada

1. Abre **9 terminales PowerShell** (una para cada servicio)
2. En cada terminal, navega a la carpeta del servicio:

```powershell
# Terminal 1
cd C:\Users\david\Downloads\asesorias-microservices-\eureka-server

# Terminal 2
cd C:\Users\david\Downloads\asesorias-microservices-\ms-auth

# Terminal 3
cd C:\Users\david\Downloads\asesorias-microservices-\api-gateway

# ... etc para cada servicio
```

### Paso 2: Ejecutar en orden

**En ORDEN (espera ~10 segundos entre cada uno):**

```powershell
# Terminal 1 - Ejecutar primero
mvn spring-boot:run

# (Espera 10 segundos)

# Terminal 2 - Ejecutar segundo
mvn spring-boot:run

# ... etc para cada terminal
```

---

## ✅ VERIFICAR QUE TODO FUNCIONE

### 1. Verificar Eureka

Abre en navegador:
```
http://localhost:8761
```

**Deberías ver:**
- 8 servicios listados
- Todos con estado "UP" (verde)

### 2. Obtener Token (Login)

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

### 3. Usar Token en un Request

```bash
curl -X GET "http://localhost:8000/api/divisiones" \
  -H "Authorization: Bearer {TOKEN_QUE_RECIBISTE}"
```

---

## 🔑 Usuarios de Prueba

```
Email: admin@uteq.edu
Contraseña: admin123
Rol: ADMIN

Email: profesor1@uteq.edu
Contraseña: pass123
Rol: PROFESOR

Email: alumno1@uteq.edu
Contraseña: pass123
Rol: ALUMNO
```

---

## 📊 Estado de los Servicios

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

## ❌ PROBLEMAS COMUNES

### "mvn not found"
**Solución:** Reinstala Maven o agrégalo al PATH

### "Connection refused"
**Solución:** 
- Verifica que Eureka esté corriendo (terminal 1)
- Espera 10 segundos antes de iniciar el siguiente

### "Port already in use"
**Solución:**
```powershell
# Encuentra el proceso usando el puerto
netstat -ano | findstr :8082

# Termina el proceso
taskkill /PID {numero} /F
```

### "Cannot register with Eureka"
**Solución:** Asegúrate que Eureka inició correctamente en terminal 1

---

## 🛑 PARAR TODO

Cierra todas las ventanas de PowerShell, o en cada una presiona:
```
Ctrl + C
```

---

## 🐳 OPCIÓN 3: Usar Docker Compose (Avanzado)

**Solo después de que todo compile exitosamente con Maven:**

```powershell
# Construir imágenes Docker
docker-compose build

# Iniciar contenedores
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar
docker-compose down
```

---

## 📝 Próximos Pasos

1. ✅ Ejecuta el script `.\start-all.ps1`
2. ✅ Espera a que todos los servicios inicien (verás los logs)
3. ✅ Abre http://localhost:8761 en navegador
4. ✅ Verifica que todos los servicios estén "UP"
5. ✅ Prueba login para obtener token
6. ✅ Prueba un endpoint protegido

---

**¡Listo para empezar!** 🎉

Si tienes dudas, consulta:
- `INSTRUCCIONES_EJECUCION.md` - Instrucciones detalladas
- `AUDITORIA_FINAL.md` - Qué está implementado
- `IMPLEMENTACION_FINAL.md` - Detalles técnicos
