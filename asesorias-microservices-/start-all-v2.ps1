# Script mejorado para iniciar todos los microservicios
$env:PATH = "C:\maven\apache-maven-3.9.4\bin;" + $env:PATH
$basePath = "C:\Users\david\Downloads\asesorias-microservices-"

$services = @(
    @{ name = "eureka-server"; port = 8761 },
    @{ name = "api-gateway"; port = 8090 },
    @{ name = "ms-admin"; port = 8081 },
    @{ name = "ms-divisiones"; port = 8082 },
    @{ name = "ms-alumnos"; port = 8085 },
    @{ name = "ms-profesores"; port = 8084 },
    @{ name = "ms-asesorias"; port = 8083 },
    @{ name = "ms-coordinadores"; port = 8086 }
)

Write-Host "🚀 Iniciando servicios de asesorías..." -ForegroundColor Cyan
Write-Host "⏳ Iniciando Eureka Server primero (necesario para los demás)..." -ForegroundColor Yellow

# Iniciar Eureka primero y esperar
$eurekaDone = $false
foreach ($svc in $services) {
    if ($svc.name -eq "eureka-server") {
        Write-Host "Iniciando $($svc.name) en puerto $($svc.port)..."
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd `"$basePath\$($svc.name)`"; `$env:PATH = 'C:\maven\apache-maven-3.9.4\bin;' + `$env:PATH; Write-Host '🟢 $($svc.name) iniciando...'; mvn spring-boot:run"
        Write-Host "✓ $($svc.name) en proceso..." -ForegroundColor Green
        $eurekaDone = $true
        break
    }
}

# Esperar 15 segundos para que Eureka se estabilice
Write-Host "⏳ Esperando a que Eureka se estabilice..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Iniciar los demás servicios
foreach ($svc in $services) {
    if ($svc.name -ne "eureka-server") {
        Write-Host "Iniciando $($svc.name) en puerto $($svc.port)..." -ForegroundColor Cyan
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd `"$basePath\$($svc.name)`"; `$env:PATH = 'C:\maven\apache-maven-3.9.4\bin;' + `$env:PATH; Write-Host '🟢 $($svc.name) iniciando...'; mvn spring-boot:run"
        Write-Host "✓ $($svc.name) en proceso..." -ForegroundColor Green
        Start-Sleep -Seconds 3
    }
}

Write-Host "`n✅ Todos los servicios han sido iniciados." -ForegroundColor Green
Write-Host "📋 URLs disponibles:" -ForegroundColor Cyan
Write-Host "  - Eureka: http://localhost:8761/" -ForegroundColor Yellow
Write-Host "  - API Gateway: http://localhost:8090/" -ForegroundColor Yellow
Write-Host "  - Admin: http://localhost:8081/" -ForegroundColor Yellow
Write-Host "  - Divisiones: http://localhost:8082/" -ForegroundColor Yellow
Write-Host "  - Alumnos: http://localhost:8085/" -ForegroundColor Yellow
Write-Host "  - Profesores: http://localhost:8084/" -ForegroundColor Yellow
Write-Host "  - Asesorías: http://localhost:8083/" -ForegroundColor Yellow
Write-Host "  - Coordinadores: http://localhost:8086/" -ForegroundColor Yellow
