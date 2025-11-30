$ErrorActionPreference = "Continue"

Write-Host "=== Testing Role-Based Login Detailed ===" -ForegroundColor Cyan

$logins = @(
    @{email="admin@uteq.edu"; password="admin123"; name="ADMIN"}
)

foreach($login in $logins) {
    Write-Host "`nTesting: $($login.name)" -ForegroundColor Yellow
    
    $headers = @{"Content-Type"="application/json"}
    $body = ConvertTo-Json @{correoMatricula=$login.email; password=$login.password}
    
    # Test ms-admin endpoint directly
    Write-Host "1. Testing /api/admin/usuarios/login query params..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8081/api/admin/usuarios/login?correo=$($login.email)&password=$($login.password)" `
            -Method POST `
            -Headers $headers `
            -TimeoutSec 5 `
            -ErrorAction Stop
        Write-Host "   SUCCESS: $($response.StatusCode)"
        $data = $response.Content | ConvertFrom-Json
        Write-Host "   Data: $($data | ConvertTo-Json -Compress)"
    }
    catch {
        Write-Host "   FAILED: $($_.Exception.Response.StatusCode)"
    }
    
    # Test through ms-auth
    Write-Host "2. Testing through ms-auth /api/auth/login..."
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8088/api/auth/login" `
            -Method POST `
            -Headers $headers `
            -Body $body `
            -TimeoutSec 5 `
            -ErrorAction Stop
        Write-Host "   SUCCESS: $($response.StatusCode)"
        $data = $response.Content | ConvertFrom-Json
        Write-Host "   Rol=$($data.rolNombre)"
    }
    catch {
        Write-Host "   FAILED: $($_.Exception.Response.StatusCode)"
    }
}
