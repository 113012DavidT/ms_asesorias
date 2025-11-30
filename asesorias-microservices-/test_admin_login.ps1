$headers = @{"Content-Type"="application/json"}

Write-Host "Testing direct to ms-admin on port 8081..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8081/api/admin/usuarios/login?correo=admin@uteq.edu&password=admin123" `
        -Method POST `
        -Headers $headers `
        -TimeoutSec 10
    
    Write-Host "Status: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        $data = $response.Content | ConvertFrom-Json
        Write-Host "SUCCESS: Nombre=$($data.nombre), Rol=$($data.rolNombre), RolId=$($data.rolId)"
    }
}
catch {
    Write-Host "Error: $($_.Exception.Message)"
}
