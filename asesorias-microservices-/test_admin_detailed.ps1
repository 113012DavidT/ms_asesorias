$headers = @{"Content-Type"="application/json"}

$uri = "http://localhost:8081/api/admin/usuarios/login?correo=admin@uteq.edu&password=admin123"

try {
    $response = Invoke-WebRequest -Uri $uri -Method POST -Headers $headers -TimeoutSec 5
    Write-Host "✅ Status: $($response.StatusCode)"
    $data = $response.Content | ConvertFrom-Json
    Write-Host "Response: $($data | ConvertTo-Json)"
} 
catch [System.Net.WebException] {
    $response = $_.Exception.Response
    $stream = $response.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $errorMsg = $reader.ReadToEnd()
    Write-Host "❌ Status: $($response.StatusCode)"
    Write-Host "Error: $errorMsg"
}
catch {
    Write-Host "❌ Exception: $($_.Exception.Message)"
}
