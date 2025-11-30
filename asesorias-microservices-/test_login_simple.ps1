$headers = @{"Content-Type"="application/json"}
$body = ConvertTo-Json @{correoMatricula="admin@uteq.edu"; password="admin123"}

Write-Host "Testing ms-auth direct on port 8088..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8088/api/auth/login" `
        -Method POST `
        -Headers $headers `
        -Body $body `
        -TimeoutSec 10
    
    Write-Host "Status: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        $data = $response.Content | ConvertFrom-Json
        Write-Host "SUCCESS: Nombre=$($data.nombre), Rol=$($data.rolNombre)"
    }
}
catch [System.Net.WebException] {
    Write-Host "WebException: $($_.Exception.Message)"
}
catch {
    Write-Host "Error: $($_.Exception.Message)"
}

Write-Host "`nTesting through gateway on port 8000..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8000/api/auth/login" `
        -Method POST `
        -Headers $headers `
        -Body $body `
        -TimeoutSec 10
    
    Write-Host "Status: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        $data = $response.Content | ConvertFrom-Json
        Write-Host "SUCCESS: Nombre=$($data.nombre), Rol=$($data.rolNombre)"
    }
}
catch [System.Net.WebException] {
    Write-Host "WebException: $($_.Exception.Message)"
}
catch {
    Write-Host "Error: $($_.Exception.Message)"
}
