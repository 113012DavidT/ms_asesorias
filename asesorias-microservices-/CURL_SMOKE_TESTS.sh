#!/bin/bash
# ============================================================================
# SMOKE TESTS - Asesorías Microservicios (curl commands)
# ============================================================================
# Ejecuta estos comandos en orden para probar el flujo E2E.
# Reemplaza localhost con la IP 10.13.10.158 si necesario.
# ============================================================================

BASE_DIVISIONES="http://localhost:8082/api"
BASE_ADMIN="http://localhost:8081/api/admin"
BASE_ASESORIAS="http://localhost:8083/api"
BASE_ALUMNOS="http://localhost:8085/api"
BASE_PROFESORES="http://localhost:8084/api"

echo "==================================================================="
echo "1) Crear una DIVISIÓN"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "clave": "DINF",
    "nombre": "División de Informática",
    "descripcion": "Carrera de Ingeniería en Sistemas"
  }' \
  "${BASE_DIVISIONES}/divisiones" | jq .

# Guardar el ID de la división (debería ser 1 o similar)
# Reemplaza DIV_ID con el id devuelto
DIV_ID=1

echo ""
echo "==================================================================="
echo "2) Crear un PROGRAMA EDUCATIVO (vinculado a la división)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "clave": "INF01",
    "nombre": "Ingeniería en Sistemas Computacionales",
    "descripcion": "Programa académico",
    "division": {
      "id": '"${DIV_ID}"'
    }
  }' \
  "${BASE_DIVISIONES}/programas" | jq .

# Guardar el ID del programa (debería ser 1 o similar)
PROG_ID=1

echo ""
echo "==================================================================="
echo "3) Crear USUARIO PROFESOR (en ms-admin)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "profesor1@uteq.edu",
    "password": "pass123",
    "nombre": "Dr. Juan Pérez",
    "activo": true
  }' \
  "${BASE_ADMIN}/usuarios" | jq .

# Guardar el ID del profesor usuario
PROF_USER_ID=1

echo ""
echo "==================================================================="
echo "4) Crear USUARIO ALUMNO (en ms-admin)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "correoMatricula": "alumno1@uteq.edu",
    "password": "pass123",
    "nombre": "Carlos López",
    "activo": true
  }' \
  "${BASE_ADMIN}/usuarios" | jq .

# Guardar el ID del alumno usuario
ALUM_USER_ID=2

echo ""
echo "==================================================================="
echo "5) Asignar PERFIL PROFESOR (vinculado a división y programa)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": '"${PROF_USER_ID}"',
    "divisionId": '"${DIV_ID}"',
    "programaId": '"${PROG_ID}"'
  }' \
  "${BASE_ADMIN}/perfiles/profesor" | jq .

echo ""
echo "==================================================================="
echo "6) Asignar PERFIL ALUMNO (vinculado a división y programa)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": '"${ALUM_USER_ID}"',
    "divisionId": '"${DIV_ID}"',
    "programaId": '"${PROG_ID}"'
  }' \
  "${BASE_ADMIN}/perfiles/alumno" | jq .

echo ""
echo "==================================================================="
echo "7) Crear DISPONIBILIDAD (profesor indica slot disponible)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "profesorId": '"${PROF_USER_ID}"',
    "fecha": "2025-11-20",
    "horaInicio": "09:00",
    "horaFin": "10:00",
    "disponible": true
  }' \
  "${BASE_ASESORIAS}/disponibilidades" | jq .

# Guardar el ID de la disponibilidad
DISP_ID=1

echo ""
echo "==================================================================="
echo "8) Crear ASESORIA (alumno reserva un slot del profesor)"
echo "==================================================================="
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "profesorId": '"${PROF_USER_ID}"',
    "alumnoId": '"${ALUM_USER_ID}"',
    "disponibilidadId": '"${DISP_ID}"',
    "materia": "Bases de Datos",
    "observaciones": "Consulta sobre normalización"
  }' \
  "${BASE_ASESORIAS}/asesorias" | jq .

echo ""
echo "==================================================================="
echo "CONSULTAS (verificar datos creados)"
echo "==================================================================="

echo ""
echo "9) Listar DIVISIONES"
echo "==================================================================="
curl -X GET "${BASE_DIVISIONES}/divisiones" | jq .

echo ""
echo "10) Listar PROGRAMAS"
echo "==================================================================="
curl -X GET "${BASE_DIVISIONES}/programas" | jq .

echo ""
echo "11) Listar USUARIOS"
echo "==================================================================="
curl -X GET "${BASE_ADMIN}/usuarios" | jq .

echo ""
echo "12) Listar DISPONIBILIDADES por profesor"
echo "==================================================================="
curl -X GET "${BASE_ASESORIAS}/disponibilidades/profesor/${PROF_USER_ID}" | jq .

echo ""
echo "13) Listar ASESORIAS por alumno"
echo "==================================================================="
curl -X GET "${BASE_ASESORIAS}/asesorias/alumno/${ALUM_USER_ID}" | jq .

echo ""
echo "14) Listar ASESORIAS por profesor"
echo "==================================================================="
curl -X GET "${BASE_ASESORIAS}/asesorias/profesor/${PROF_USER_ID}" | jq .

echo ""
echo "15) Obtener PERFIL del alumno"
echo "==================================================================="
curl -X GET "${BASE_ADMIN}/perfiles/alumno/${ALUM_USER_ID}" | jq .

echo ""
echo "16) Obtener PERFIL del profesor"
echo "==================================================================="
curl -X GET "${BASE_ADMIN}/perfiles/profesor/${PROF_USER_ID}" | jq .

echo ""
echo "==================================================================="
echo "FIN DE LAS PRUEBAS"
echo "==================================================================="
