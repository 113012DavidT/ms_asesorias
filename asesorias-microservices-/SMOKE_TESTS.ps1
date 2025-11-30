# ============================================================================
# SMOKE TESTS - Asesorías Microservicios (PowerShell Invoke-RestMethod)
# ============================================================================
# Ejecuta este script en PowerShell para probar el flujo E2E.
# Ajusta localhost a 10.13.10.158 si necesario (según tu red).
# ============================================================================

$BaseDivisiones = "http://localhost:8082/api"
$BaseAdmin = "http://localhost:8081/api/admin"
$BaseAsesorias = "http://localhost:8083/api"
$BaseAlumnos = "http://localhost:8085/api"
$BaseProfesores = "http://localhost:8084/api"

Write-Host "===================================================================" -ForegroundColor Green
Write-Host "1) Crear una DIVISIÓN" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$divisionBody = @{
    clave = "DINF"
    nombre = "División de Informática"
    descripcion = "Carrera de Ingeniería en Sistemas"
} | ConvertTo-Json

$division = Invoke-RestMethod -Method Post `
    -Uri "$BaseDivisiones/divisiones" `
    -ContentType "application/json" `
    -Body $divisionBody

Write-Host "División creada:" -ForegroundColor Yellow
Write-Host ($division | ConvertTo-Json -Depth 3)
$divId = $division.id
Write-Host "DIV_ID = $divId" -ForegroundColor Magenta

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "2) Crear un PROGRAMA EDUCATIVO (vinculado a la división)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$programaBody = @{
    clave = "INF01"
    nombre = "Ingeniería en Sistemas Computacionales"
    descripcion = "Programa académico"
    division = @{
        id = $divId
    }
} | ConvertTo-Json -Depth 3

$programa = Invoke-RestMethod -Method Post `
    -Uri "$BaseDivisiones/programas" `
    -ContentType "application/json" `
    -Body $programaBody

Write-Host "Programa creado:" -ForegroundColor Yellow
Write-Host ($programa | ConvertTo-Json -Depth 3)
$progId = $programa.id
Write-Host "PROG_ID = $progId" -ForegroundColor Magenta

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "3) Crear USUARIO PROFESOR (en ms-admin)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$profesorUserBody = @{
    correoMatricula = "profesor1@uteq.edu"
    password = "pass123"
    nombre = "Dr. Juan Pérez"
    activo = $true
} | ConvertTo-Json

$profesorUser = Invoke-RestMethod -Method Post `
    -Uri "$BaseAdmin/usuarios" `
    -ContentType "application/json" `
    -Body $profesorUserBody

Write-Host "Usuario Profesor creado:" -ForegroundColor Yellow
Write-Host ($profesorUser | ConvertTo-Json -Depth 3)
$profUserId = $profesorUser.id
Write-Host "PROF_USER_ID = $profUserId" -ForegroundColor Magenta

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "4) Crear USUARIO ALUMNO (en ms-admin)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$alumnoUserBody = @{
    correoMatricula = "alumno1@uteq.edu"
    password = "pass123"
    nombre = "Carlos López"
    activo = $true
} | ConvertTo-Json

$alumnoUser = Invoke-RestMethod -Method Post `
    -Uri "$BaseAdmin/usuarios" `
    -ContentType "application/json" `
    -Body $alumnoUserBody

Write-Host "Usuario Alumno creado:" -ForegroundColor Yellow
Write-Host ($alumnoUser | ConvertTo-Json -Depth 3)
$alumUserId = $alumnoUser.id
Write-Host "ALUM_USER_ID = $alumUserId" -ForegroundColor Magenta

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "5) Asignar PERFIL PROFESOR (vinculado a división y programa)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$profPerfilBody = @{
    usuarioId = $profUserId
    divisionId = $divId
    programaId = $progId
} | ConvertTo-Json

$profPerfil = Invoke-RestMethod -Method Post `
    -Uri "$BaseAdmin/perfiles/profesor" `
    -ContentType "application/json" `
    -Body $profPerfilBody

Write-Host "Perfil Profesor asignado:" -ForegroundColor Yellow
Write-Host ($profPerfil | ConvertTo-Json -Depth 3)

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "6) Asignar PERFIL ALUMNO (vinculado a división y programa)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$alumPerfilBody = @{
    usuarioId = $alumUserId
    divisionId = $divId
    programaId = $progId
} | ConvertTo-Json

$alumPerfil = Invoke-RestMethod -Method Post `
    -Uri "$BaseAdmin/perfiles/alumno" `
    -ContentType "application/json" `
    -Body $alumPerfilBody

Write-Host "Perfil Alumno asignado:" -ForegroundColor Yellow
Write-Host ($alumPerfil | ConvertTo-Json -Depth 3)

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "7) Crear DISPONIBILIDAD (profesor indica slot disponible)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$dispBody = @{
    profesorId = $profUserId
    fecha = "2025-11-20"
    horaInicio = "09:00"
    horaFin = "10:00"
    disponible = $true
} | ConvertTo-Json

$disp = Invoke-RestMethod -Method Post `
    -Uri "$BaseAsesorias/disponibilidades" `
    -ContentType "application/json" `
    -Body $dispBody

Write-Host "Disponibilidad creada:" -ForegroundColor Yellow
Write-Host ($disp | ConvertTo-Json -Depth 3)
$dispId = $disp.id
Write-Host "DISP_ID = $dispId" -ForegroundColor Magenta

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "8) Crear ASESORIA (alumno reserva un slot del profesor)" -ForegroundColor Cyan
Write-Host "===================================================================" -ForegroundColor Green

$asesoriaBody = @{
    profesorId = $profUserId
    alumnoId = $alumUserId
    disponibilidadId = $dispId
    materia = "Bases de Datos"
    observaciones = "Consulta sobre normalización"
} | ConvertTo-Json

$asesoria = Invoke-RestMethod -Method Post `
    -Uri "$BaseAsesorias/asesorias" `
    -ContentType "application/json" `
    -Body $asesoriaBody

Write-Host "Asesoria creada:" -ForegroundColor Yellow
Write-Host ($asesoria | ConvertTo-Json -Depth 3)

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "CONSULTAS (verificar datos creados)" -ForegroundColor Yellow
Write-Host "===================================================================" -ForegroundColor Green

Write-Host ""
Write-Host "9) Listar DIVISIONES" -ForegroundColor Cyan
$divs = Invoke-RestMethod -Method Get -Uri "$BaseDivisiones/divisiones"
Write-Host "Divisiones count: $($divs.Count)"
$divs | ConvertTo-Json -Depth 2 | Write-Host

Write-Host ""
Write-Host "10) Listar PROGRAMAS" -ForegroundColor Cyan
$progs = Invoke-RestMethod -Method Get -Uri "$BaseDivisiones/programas"
Write-Host "Programas count: $($progs.Count)"
$progs | ConvertTo-Json -Depth 2 | Write-Host

Write-Host ""
Write-Host "11) Listar USUARIOS" -ForegroundColor Cyan
$users = Invoke-RestMethod -Method Get -Uri "$BaseAdmin/usuarios"
Write-Host "Usuarios count: $($users.Count)"
$users | ConvertTo-Json -Depth 2 | Write-Host

Write-Host ""
Write-Host "12) Listar DISPONIBILIDADES por profesor" -ForegroundColor Cyan
$disps = Invoke-RestMethod -Method Get -Uri "$BaseAsesorias/disponibilidades/profesor/$profUserId"
Write-Host "Disponibilidades count: $($disps.Count)"
$disps | ConvertTo-Json -Depth 3 | Write-Host

Write-Host ""
Write-Host "13) Listar ASESORIAS por alumno" -ForegroundColor Cyan
$ases1 = Invoke-RestMethod -Method Get -Uri "$BaseAsesorias/asesorias/alumno/$alumUserId"
Write-Host "Asesorias por alumno count: $($ases1.Count)"
$ases1 | ConvertTo-Json -Depth 3 | Write-Host

Write-Host ""
Write-Host "14) Listar ASESORIAS por profesor" -ForegroundColor Cyan
$ases2 = Invoke-RestMethod -Method Get -Uri "$BaseAsesorias/asesorias/profesor/$profUserId"
Write-Host "Asesorias por profesor count: $($ases2.Count)"
$ases2 | ConvertTo-Json -Depth 3 | Write-Host

Write-Host ""
Write-Host "15) Obtener PERFIL del alumno" -ForegroundColor Cyan
$perf_alum = Invoke-RestMethod -Method Get -Uri "$BaseAdmin/perfiles/alumno/$alumUserId"
Write-Host "Perfil alumno:"
$perf_alum | ConvertTo-Json -Depth 3 | Write-Host

Write-Host ""
Write-Host "16) Obtener PERFIL del profesor" -ForegroundColor Cyan
$perf_prof = Invoke-RestMethod -Method Get -Uri "$BaseAdmin/perfiles/profesor/$profUserId"
Write-Host "Perfil profesor:"
$perf_prof | ConvertTo-Json -Depth 3 | Write-Host

Write-Host ""
Write-Host "===================================================================" -ForegroundColor Green
Write-Host "FIN DE LAS PRUEBAS" -ForegroundColor Green
Write-Host "===================================================================" -ForegroundColor Green

# Guardar resumen en archivo
@{
    division = $division
    programa = $programa
    profesorUser = $profesorUser
    alumnoUser = $alumnoUser
    profPerfil = $profPerfil
    alumPerfil = $alumPerfil
    disponibilidad = $disp
    asesoria = $asesoria
} | ConvertTo-Json -Depth 8 | Out-File -FilePath ".\smoke_test_results.json"
Write-Host "Resultados guardados en: smoke_test_results.json" -ForegroundColor Green
