Write-Host "=== Testing Role-Based Login ===" -ForegroundColor Cyan

$logins = @(
    @{email="admin@uteq.edu"; password="admin123"; name="ADMIN"},
    @{email="profesor1@uteq.edu"; password="prof123"; name="PROFESOR"},
    @{email="alumno1@uteq.edu"; password="alum123"; name="ALUMNO"},
    @{email="coordinador1@uteq.edu"; password="coord123"; name="COORDINADOR"}
)

foreach($login in $logins) {
    $headers = @{"Content-Type"="application/json"}
    $body = ConvertTo-Json @{correoMatricula=$login.email; password=$login.password}
    
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8000/api/auth/login" `
            -Method POST `
            -Headers $headers `
            -Body $body `
            -ErrorAction Stop
        
        $data = $response.Content | ConvertFrom-Json
        Write-Host "$($login.name): SUCCESS - Rol=$($data.rolNombre), Usuario=$($data.nombre)"
    }
    catch {
        Write-Host "$($login.name): FAILED - $($_.Exception.Message)" -ForegroundColor Red
    }
}
