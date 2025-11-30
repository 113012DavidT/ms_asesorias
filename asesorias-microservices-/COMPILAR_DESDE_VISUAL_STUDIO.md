# 🛠️ COMPILAR DESDE VISUAL STUDIO

## Opción 1: Usar Maven desde Visual Studio (RECOMENDADO)

### Paso 1: Abrir cada carpeta en Visual Studio

Para cada microservicio, abre en Visual Studio:

1. **eureka-server/**
   - Abre Terminal integrada (Ctrl + `)
   - Ejecuta: `mvn clean install -DskipTests`

2. **ms-auth/**
   - Abre Terminal integrada
   - Ejecuta: `mvn clean install -DskipTests`

3. **api-gateway/**
   - Ejecuta: `mvn clean install -DskipTests`

4. **ms-admin/**
   - Ejecuta: `mvn clean install -DskipTests`

5. **ms-asesorias/**
   - Ejecuta: `mvn clean install -DskipTests`

6. **ms-coordinadores/**
   - Ejecuta: `mvn clean install -DskipTests`

7. **ms-divisiones/**
   - Ejecuta: `mvn clean install -DskipTests`

8. **ms-profesores/**
   - Ejecuta: `mvn clean install -DskipTests`

9. **ms-alumnos/**
   - Ejecuta: `mvn clean install -DskipTests`

---

## Opción 2: Usar VS Code con Maven Extension

1. Instala la extensión "Extension Pack for Java" en VS Code
2. VS Code detectará automáticamente Maven
3. Abre cada carpeta y ejecuta los comandos desde la terminal integrada

---

## Opción 3: Usar Maven desde PowerShell (después de instalación)

Si instalaste Maven con Chocolatey:

1. **Abre PowerShell como Administrador**
2. Navega a cada carpeta
3. Ejecuta: `mvn clean install -DskipTests`

---

## ¿No compila?

Si ves errores de compilación:

```
1. Verifica que Java 17+ esté instalado:
   java -version

2. Verifica que Maven esté disponible:
   mvn --version

3. Si Maven no está disponible:
   - Descarga desde: https://maven.apache.org/download.cgi
   - Extrae en: C:\Program Files\Maven
   - Agrega al PATH: C:\Program Files\Maven\bin
   - Reinicia PowerShell
```

---

## Después de compilar

Una vez que todos compilen exitosamente:

### Docker Compose:
```powershell
docker-compose build
docker-compose up -d
```

### O ejecutar localmente:
```powershell
.\start-all.ps1
```
