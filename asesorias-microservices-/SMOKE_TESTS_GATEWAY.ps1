# ============================================================================
# SMOKE TESTS (Gateway + JWT)
# Requiere: usuario admin existente (admin@uteq.edu / pass123 ajusta si cambia)
# ============================================================================

$Gateway = "http://localhost:8000"
$AdminLoginCorreo = "admin@uteq.edu"
$AdminLoginPassword = "pass123"

Write-Host "1) Login" -ForegroundColor Cyan
$loginBody = @{ correoMatricula = $AdminLoginCorreo; password = $AdminLoginPassword } | ConvertTo-Json
try {
    $loginResp = Invoke-RestMethod -Method Post -Uri "$Gateway/api/auth/login" -ContentType "application/json" -Body $loginBody
    $token = $loginResp.token
    if (-not $token) { throw "Sin token en respuesta" }
    Write-Host "Token obtenido (longitud=$($token.Length))" -ForegroundColor Green
} catch {
    Write-Host "ERROR login: $_" -ForegroundColor Red
    exit 1
}

$Headers = @{ Authorization = "Bearer $token" }

Write-Host "2) Listar usuarios (ms-admin)" -ForegroundColor Cyan
try {
    $usuarios = Invoke-RestMethod -Method Get -Uri "$Gateway/api/admin/usuarios" -Headers $Headers
    Write-Host "Usuarios count: $($usuarios.Count)" -ForegroundColor Green
} catch { Write-Host "ERROR usuarios: $_" -ForegroundColor Red }

Write-Host "3) Health Auth" -ForegroundColor Cyan
try {
    $health = Invoke-RestMethod -Method Get -Uri "$Gateway/api/auth/health"
    Write-Host "Auth health: $health" -ForegroundColor Green
} catch { Write-Host "ERROR health: $_" -ForegroundColor Red }

Write-Host "4) Validar token" -ForegroundColor Cyan
try {
    $valBody = @{ token = $token } | ConvertTo-Json
    $val = Invoke-RestMethod -Method Post -Uri "$Gateway/api/auth/validate" -ContentType "application/json" -Body $valBody
    Write-Host "Valid: $($val.valid) Rol=$($val.rolNombre)" -ForegroundColor Green
} catch { Write-Host "ERROR validate: $_" -ForegroundColor Red }

Write-Host "5) Intentar acceso con token a profesores (si existe)" -ForegroundColor Cyan
try {
    $profList = Invoke-RestMethod -Method Get -Uri "$Gateway/api/profesores/" -Headers $Headers
    Write-Host "Profesores count: $($profList.Count)" -ForegroundColor Green
} catch { Write-Host "Profesores acceso error (puede ser normal si vacio): $_" -ForegroundColor Yellow }

Write-Host "6) Refresh token" -ForegroundColor Cyan
try {
    $refreshToken = $loginResp.refreshToken
    if ($refreshToken) {
        $refreshResp = Invoke-RestMethod -Method Post -Uri "$Gateway/api/auth/refresh" -Headers @{ Authorization = "Bearer $refreshToken" }
        Write-Host "Nuevo token emitido (longitud=$($refreshResp.token.Length))" -ForegroundColor Green
    } else {
        Write-Host "Sin refreshToken en login" -ForegroundColor Yellow
    }
} catch { Write-Host "ERROR refresh: $_" -ForegroundColor Red }

Write-Host "FIN SMOKE" -ForegroundColor Magenta
