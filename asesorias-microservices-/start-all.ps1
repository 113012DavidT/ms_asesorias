# 🚀 Script PowerShell para iniciar todos los microservicios
# Este script abre una ventana separada para cada servicio

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Iniciando Sistema de Microservicios" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

$basePath = "C:\Users\david\Downloads\asesorias-microservices-"

$services = @(
    @{Name="Eureka Server"; Path="eureka-server"; Port=8761},
    @{Name="MS-Auth"; Path="ms-auth"; Port=8088},
    @{Name="API Gateway"; Path="api-gateway"; Port=8000},
    @{Name="MS-Admin"; Path="ms-admin"; Port=8081},
    @{Name="MS-Asesorías"; Path="ms-asesorias"; Port=8082},
    @{Name="MS-Coordinadores"; Path="ms-coordinadores"; Port=8083},
    @{Name="MS-Divisiones"; Path="ms-divisiones"; Port=8084},
    @{Name="MS-Profesores"; Path="ms-profesores"; Port=8085},
    @{Name="MS-Alumnos"; Path="ms-alumnos"; Port=8086}
)

foreach ($service in $services) {
    Write-Host "🚀 Iniciando $($service.Name) en puerto $($service.Port)..." -ForegroundColor Green
    $fullPath = "$basePath\$($service.Path)"
    Start-Process "powershell.exe" -ArgumentList "-NoExit", "-Command", "cd '$fullPath'; mvn spring-boot:run"
    Start-Sleep -Seconds 8
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "✅ TODOS LOS SERVICIOS HAN INICIADO" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "URLs disponibles:" -ForegroundColor Yellow
Write-Host "  • Eureka Dashboard: http://localhost:8761" -ForegroundColor Cyan
Write-Host "  • API Gateway: http://localhost:8000" -ForegroundColor Cyan
Write-Host "  • MS-Admin: http://localhost:8081" -ForegroundColor Cyan
Write-Host ""
Write-Host "Nota: Cada servicio se abrirá en su propia ventana PowerShell" -ForegroundColor Yellow
Write-Host "Para detener, cierra cada ventana o presiona Ctrl+C en cada una" -ForegroundColor Yellow
Write-Host ""
