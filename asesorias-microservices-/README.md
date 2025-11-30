# Asesorías – Microservicios COMPLETO (Admin + Divisiones + Asesorías)
Stack: Spring Boot 3, Java 17, Eureka, API Gateway, H2, OpenFeign.

## Servicios y puertos
- `eureka-server` → 8761
- `api-gateway` → 8080
- `ms-admin` → 8081 (Usuarios y Perfiles: Profesor/Alumno)
- `ms-divisiones` → 8082 (División y ProgramaEducativo)
- `ms-asesorias` → 8083 (Disponibilidad y Asesorías con validación de perfiles)

## Arranque (en ese orden)
```
cd eureka-server && mvn spring-boot:run
cd api-gateway  && mvn spring-boot:run
cd ms-admin     && mvn spring-boot:run
cd ms-divisiones&& mvn spring-boot:run
cd ms-asesorias && mvn spring-boot:run
```

## Flujo para crear una asesoría (END-TO-END)

1) **Crear las entidades académicas**
```
POST http://localhost:8080/api/divisiones
{ "clave":"D01","nombre":"División Sistemas","descripcion":"..." }

POST http://localhost:8080/api/programas
{ "clave":"PE01","nombre":"Ing. Software","descripcion":"..." }
```
Guarda los `id` devueltos (divisionId y programaId).

2) **Crear usuarios en ms-admin (solo Admin registra)**
```
POST http://localhost:8080/api/admin/usuarios
{ "correoMatricula":"prof1","password":"1234","nombre":"Profe Uno","rol":"PROFESOR" }

POST http://localhost:8080/api/admin/usuarios
{ "correoMatricula":"alum1","password":"1234","nombre":"Alumno Uno","rol":"ALUMNO" }
```
Guarda los `id` (profesorId y alumnoId).

3) **Asignar perfiles (vincular a programa/división)**
```
POST http://localhost:8080/api/admin/perfiles/profesor
{ "usuarioId": 1, "divisionId": 1, "programaId": 1 }

POST http://localhost:8080/api/admin/perfiles/alumno
{ "usuarioId": 2, "divisionId": 1, "programaId": 1 }
```

4) **Crear disponibilidad del profesor**
```
POST http://localhost:8080/api/disponibilidades
{ "profesorId":1, "fecha":"2025-11-10", "horaInicio":"09:00", "horaFin":"10:00" }
```
Copia el `disponibilidadId` devuelto.

5) **Crear asesoría (alumno reserva un slot del profesor)**
```
POST http://localhost:8080/api/asesorias
{
  "profesorId": 1,
  "alumnoId": 2,
  "disponibilidadId": 1,
  "materia": "Bases de Datos",
  "observaciones": "Temas del examen"
}
```
Reglas de negocio implementadas:
- Profesor y Alumno **deben pertenecer al mismo `programaId`** (se valida vía Feign con `ms-admin`).
- La `disponibilidadId` debe ser del `profesorId` y **estar disponible**. Se marca ocupada al crear.
- La Asesoría guarda `fecha` y `hora` del slot.

6) **Consultar**
```
GET http://localhost:8080/api/asesorias/profesor/1
GET http://localhost:8080/api/asesorias/alumno/2
GET http://localhost:8080/api/disponibilidades/profesor/1
```


## Nuevos servicios
- `ms-profesores` (8084)
  - `GET /api/profesores/{usuarioId}/perfil`
  - `GET /api/profesores/{usuarioId}/asesorias`
  - `POST /api/profesores/{usuarioId}/disponibilidades`

- `ms-alumnos` (8085)
  - `GET /api/alumnos/{usuarioId}/perfil`
  - `GET /api/alumnos/{usuarioId}/asesorias`
  - `POST /api/alumnos/{usuarioId}/asesorias` (crea asesoría end-to-end)

### Ejemplo rápido (tras crear programa/división, usuarios y perfiles)
1) **Profesor crea disponibilidad via su MS**  
```
POST http://localhost:8080/api/profesores/1/disponibilidades
{ "fecha":"2025-11-10", "horaInicio":"09:00", "horaFin":"10:00" }
```
2) **Alumno reserva (crea asesoría) desde su MS**  
```
POST http://localhost:8080/api/alumnos/2/asesorias
{ "profesorId":1, "disponibilidadId":1, "materia":"BD", "observaciones":"Parcial 2" }
```
