#!/usr/bin/env pwsh
# 🚀 SCRIPT DE COMPILACIÓN - Usando Chocolatey para Maven

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   🚀 COMPILADOR AUTOMÁTICO DE MICROSERVICIOS 🚀           ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Verificar si Chocolatey está instalado
Write-Host "🔍 Verificando Chocolatey..." -ForegroundColor Cyan

if (-not (Get-Command choco -ErrorAction SilentlyContinue)) {
    Write-Host "⚠️  Chocolatey no encontrado" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "📥 Instalando Chocolatey..." -ForegroundColor Yellow
    
    try {
        Set-ExecutionPolicy Bypass -Scope Process -Force
        [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
        Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    } catch {
        Write-Host "❌ Error instalando Chocolatey" -ForegroundColor Red
        Write-Host ""
        Write-Host "🔧 Alternativa - Instalar Maven manualmente:" -ForegroundColor Yellow
        Write-Host "   1. Descarga desde: https://maven.apache.org/download.cgi" -ForegroundColor Gray
        Write-Host "   2. Extrae en C:\Program Files\Maven" -ForegroundColor Gray
        Write-Host "   3. Agrega C:\Program Files\Maven\bin al PATH" -ForegroundColor Gray
        exit 1
    }
}

Write-Host "✅ Chocolatey disponible" -ForegroundColor Green

# Verificar si Maven está instalado
Write-Host "🔍 Verificando Maven..." -ForegroundColor Cyan

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "⚠️  Maven no encontrado" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "📥 Instalando Maven con Chocolatey..." -ForegroundColor Yellow
    
    choco install maven -y
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Error instalando Maven" -ForegroundColor Red
        exit 1
    }
    
    # Recargar PATH
    $env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")
}

Write-Host "✅ Maven disponible" -ForegroundColor Green
Write-Host ""

# Compilar todos los microservicios
Write-Host "📋 COMPILANDO MICROSERVICIOS..." -ForegroundColor Yellow
Write-Host ""

$microservicios = @(
    "eureka-server",
    "ms-auth",
    "api-gateway",
    "ms-admin",
    "ms-asesorias",
    "ms-coordinadores",
    "ms-divisiones",
    "ms-profesores",
    "ms-alumnos"
)

$rootPath = Get-Location
$compiledCount = 0
$failedCount = 0

foreach ($ms in $microservicios) {
    $msPath = Join-Path $rootPath $ms
    
    if (-not (Test-Path $msPath)) {
        Write-Host "⏭️  $ms - NO ENCONTRADO" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "📦 $ms..." -ForegroundColor Cyan -NoNewline
    
    Push-Location $msPath
    
    # Ejecutar Maven silenciosamente
    mvn clean install -DskipTests -q 2>&1 | Out-Null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host " ✅" -ForegroundColor Green
        $compiledCount++
    } else {
        Write-Host " ❌" -ForegroundColor Red
        $failedCount++
    }
    
    Pop-Location
}

Write-Host ""
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "   ✅ Compilados:  $compiledCount / $($microservicios.Count)" -ForegroundColor Green
Write-Host "   ❌ Errores:     $failedCount" -ForegroundColor $(if ($failedCount -gt 0) { "Red" } else { "Green" })
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

if ($failedCount -gt 0) {
    Write-Host "⚠️  Hubo errores. Verifica los logs arriba." -ForegroundColor Red
    exit 1
}

Write-Host "✅ ¡COMPILACIÓN EXITOSA!" -ForegroundColor Green
Write-Host ""
Write-Host "🚀 PRÓXIMOS PASOS:" -ForegroundColor Yellow
Write-Host ""
Write-Host "   1. Docker Compose:" -ForegroundColor Cyan
Write-Host "      docker-compose up -d" -ForegroundColor Gray
Write-Host ""
Write-Host "   2. O ejecutar localmente:" -ForegroundColor Cyan
Write-Host "      .\start-all.ps1" -ForegroundColor Gray
Write-Host ""
