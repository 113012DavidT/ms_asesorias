# 🎓 Asesorías Microservicios - Sistema de Gestión Escolar

## 📝 Descripción

Sistema completo de microservicios para gestionar asesorías escolares, divisiones, programas educativos y grupos de alumnos. Implementado con Spring Boot, Eureka, y API Gateway.

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (WEB/MOBILE)                  │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                      API GATEWAY                         │
│                    (Puerto 8000)                         │
└──────────────────────────┬──────────────────────────────┘
                           │
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
    │   MS-Admin   │ │ MS-Asesorías │ │MS-Coordinad. │
    │  (8081)      │ │   (8082)     │ │  (8083)      │
    └──────┬───────┘ └──────┬───────┘ └──────┬───────┘
           │               │               │
           └───────────────┼───────────────┘
                           │
                    ┌──────▼───────┐
                    │ EUREKA       │
                    │ (8761)       │
                    └──────────────┘
```

## 🚀 Microservicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **Eureka Server** | 8761 | Service Discovery |
| **API Gateway** | 8000 | Enrutador central |
| **MS-Admin** | 8081 | Gestión de usuarios, divisiones, programas, grupos |
| **MS-Asesorías** | 8082 | Gestión de asesorías (por implementar) |
| **MS-Coordinadores** | 8083 | Coordinación (por implementar) |
| **MS-Divisiones** | 8084 | Gestión de divisiones (por implementar) |
| **MS-Profesores** | 8085 | Gestión de profesores (por implementar) |
| **MS-Alumnos** | 8086 | Gestión de alumnos (por implementar) |

## 👥 Roles y Permisos

### 1. **ADMIN** 👨‍💼
- Crear/editar/eliminar usuarios
- Crear divisiones y programas
- Asignar coordinadores
- Acceso total al sistema

### 2. **COORDINADOR** 👨‍🏫
- Asignar profesores a división/programa
- Asignar alumnos a división/programa
- Gestionar grupos
- Ver asesorías de su división/programa

### 3. **PROFESOR** 📚
- Crear grupos
- Asignar asesorías a sus grupos
- Ver alumnos de sus grupos
- Consultar solicitudes de asesorías

### 4. **ALUMNO** 🎓
- Ver grupos disponibles
- Solicitar asesorías
- Ver historial de asesorías

## 📊 Modelo de Datos

### Entidades Principales

```
Usuario
├── id (PK)
├── correoMatricula (unique)
├── password
├── nombre
├── apellido
├── activo
├── rol (FK → Rol)
├── fechaCreacion
├── fechaActualizacion
└── ultimoAcceso

Rol
├── id (PK)
└── nombre (ADMIN, COORDINADOR, PROFESOR, ALUMNO)

Division
├── id (PK)
├── nombre
├── descripcion
└── activo

Programa
├── id (PK)
├── nombre
├── descripcion
└── activo

ProfesorPerfil
├── id (PK)
├── usuarioId (unique FK)
├── division (FK)
├── programa (FK)
└── activo

AlumnoPerfil
├── id (PK)
├── usuarioId (unique FK)
├── division (FK)
├── programa (FK)
└── activo

CoordinadorPerfil
├── id (PK)
├── usuarioId (unique FK)
├── division (FK)
├── programa (FK)
└── activo

Grupo
├── id (PK)
├── nombre
├── descripcion
├── profesor (FK)
├── division (FK)
├── programa (FK)
└── activo
```

## 🔧 Requisitos

- **Java 17+**
- **Maven 3.9+**
- **Docker & Docker Compose** (opcional)
- **PostgreSQL 13+** (producción)

## 📦 Instalación

### Local (Sin Docker)

```bash
# 1. Clonar repositorio
git clone <repo-url>
cd asesorias-microservices

# 2. Compilar todos los servicios
mvn clean install -DskipTests

# 3. Ejecutar cada servicio en terminal separada
# Terminal 1: Eureka
cd eureka-server && mvn spring-boot:run

# Terminal 2: MS-Admin
cd ms-admin && mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway && mvn spring-boot:run

# ... resto de servicios
```

### Con Docker Compose

```bash
# 1. Construir imágenes
docker-compose build

# 2. Iniciar servicios
docker-compose up -d

# 3. Ver logs
docker-compose logs -f

# 4. Detener servicios
docker-compose down
```

## 📚 API Endpoints (MS-Admin)

### Usuarios
```
POST   /api/admin/usuarios              - Crear usuario
GET    /api/admin/usuarios              - Listar usuarios
GET    /api/admin/usuarios/{id}         - Obtener por ID
PUT    /api/admin/usuarios/{id}         - Actualizar
DELETE /api/admin/usuarios/{id}         - Eliminar
POST   /api/admin/usuarios/login        - Autenticación
```

### Divisiones
```
POST   /api/divisiones                  - Crear
GET    /api/divisiones                  - Listar
GET    /api/divisiones/{id}             - Obtener
PUT    /api/divisiones/{id}             - Actualizar
DELETE /api/divisiones/{id}             - Eliminar
```

### Programas
```
POST   /api/programas                   - Crear
GET    /api/programas                   - Listar
PUT    /api/programas/{id}              - Actualizar
DELETE /api/programas/{id}              - Eliminar
```

### Grupos
```
POST   /api/grupos                      - Crear
GET    /api/grupos                      - Listar
GET    /api/grupos/profesor/{id}        - Por profesor
GET    /api/grupos/division/{id}        - Por división
PUT    /api/grupos/{id}                 - Actualizar
DELETE /api/grupos/{id}                 - Eliminar
```

### Coordinadores
```
POST   /api/coordinadores               - Crear
GET    /api/coordinadores               - Listar
GET    /api/coordinadores/usuario/{id}  - Por usuario
PUT    /api/coordinadores/{id}          - Actualizar
DELETE /api/coordinadores/{id}          - Eliminar
```

## 🔑 Usuarios de Prueba

| Email | Password | Rol |
|-------|----------|-----|
| admin@uteq.edu | admin123 | ADMIN |
| coordinador1@uteq.edu | pass123 | COORDINADOR |
| profesor1@uteq.edu | pass123 | PROFESOR |
| alumno1@uteq.edu | pass123 | ALUMNO |

## 🌐 URLs Importantes

| Servicio | URL |
|----------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8000 |
| MS-Admin | http://localhost:8081 |
| H2 Console | http://localhost:8081/h2-console |

## 📋 Documentación

- [`MS-ADMIN-API-DOCS.md`](./ms-admin/MS-ADMIN-API-DOCS.md) - Documentación de API completa
- [`ESTRUCTURA_MS_ADMIN.md`](./ESTRUCTURA_MS_ADMIN.md) - Diagrama de estructura
- [`GUIA_EJECUCION.md`](./GUIA_EJECUCION.md) - Guía de instalación y ejecución
- [`CAMBIOS_REALIZADOS.md`](./CAMBIOS_REALIZADOS.md) - Detalle de cambios

## 🛠️ Tecnologías

- **Framework**: Spring Boot 3.4.0
- **Cloud**: Spring Cloud 2024.0.0
- **Discovery**: Eureka
- **ORM**: JPA/Hibernate
- **Base de Datos**: H2 (dev), PostgreSQL (prod)
- **Build**: Maven
- **Containerización**: Docker
- **Lenguaje**: Java 17

## 📈 Próximos Pasos

### Fase 1: Autenticación
- [ ] Crear MS-Auth con JWT
- [ ] Validar tokens en API Gateway
- [ ] Agregar @PreAuthorize en endpoints
- [ ] Encriptar passwords

### Fase 2: Sincronización
- [ ] Event-driven architecture
- [ ] Tablas virtuales en microservicios
- [ ] Cache distribuido (Redis)

### Fase 3: Funcionalidades
- [ ] Completar MS-Asesorías
- [ ] Notificaciones
- [ ] Reportes

### Fase 4: DevOps
- [ ] CI/CD con GitHub Actions
- [ ] Kubernetes deployment
- [ ] Logging centralizado (ELK)
- [ ] Monitoreo (Prometheus/Grafana)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver [`LICENSE`](LICENSE) para más detalles.

## 👨‍💻 Autor

David Pérez - [GitHub](https://github.com)

## 📞 Soporte

Para soporte, enviar email a: support@asesorias.edu

## 🎯 Objetivos

- ✅ Sistema modular y escalable
- ✅ Fácil de mantener y expandir
- ✅ Arquitectura de microservicios
- ✅ Seguridad con JWT
- ✅ Documentación completa
- ⏳ Deployment automatizado

---

**Última actualización**: Enero 2025
**Versión**: 1.0.0
**Estado**: En Desarrollo ⚙️
