#!/usr/bin/env pwsh
# Script para compilar todos los MS buscando Maven en rutas comunes

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║          COMPILADOR DE MICROSERVICIOS (Maven)             ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Buscar Maven en todas las rutas posibles
$possiblePaths = @(
    "mvn",  # En el PATH
    "C:\Program Files\Maven\bin\mvn.cmd",
    "C:\Program Files (x86)\Maven\bin\mvn.cmd",
    "C:\apache-maven\bin\mvn.cmd",
    "C:\ProgramData\chocolatey\bin\mvn.cmd",
    "$env:MAVEN_HOME\bin\mvn.cmd",
    "$env:M2_HOME\bin\mvn.cmd"
)

$mvnPath = $null
$mvnFound = $false

Write-Host "🔍 Buscando Maven..." -ForegroundColor Yellow

foreach ($path in $possiblePaths) {
    if ($path -eq "mvn") {
        if (Get-Command mvn -ErrorAction SilentlyContinue) {
            $mvnPath = "mvn"
            $mvnFound = $true
            Write-Host "✅ Maven encontrado en PATH" -ForegroundColor Green
            break
        }
    } elseif (Test-Path $path) {
        $mvnPath = $path
        $mvnFound = $true
        Write-Host "✅ Maven encontrado: $path" -ForegroundColor Green
        break
    }
}

if (-not $mvnFound) {
    Write-Host "❌ Maven no encontrado" -ForegroundColor Red
    Write-Host ""
    Write-Host "🔧 SOLUCIONES:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1️⃣  Opción A - Instalar Maven manualmente:" -ForegroundColor Cyan
    Write-Host "   - Descarga: https://maven.apache.org/download.cgi" -ForegroundColor Gray
    Write-Host "   - Extrae en: C:\Program Files\Maven" -ForegroundColor Gray
    Write-Host "   - Agrega PATH: C:\Program Files\Maven\bin" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2️⃣  Opción B - Usar Chocolatey (requiere Admin):" -ForegroundColor Cyan
    Write-Host "   - Abre PowerShell como Administrador" -ForegroundColor Gray
    Write-Host "   - Ejecuta: choco install maven -y" -ForegroundColor Gray
    Write-Host "   - Cierra y reabre PowerShell" -ForegroundColor Gray
    Write-Host ""
    Write-Host "3️⃣  Opción C - Compilar desde Visual Studio:" -ForegroundColor Cyan
    Write-Host "   - Lee: COMPILAR_DESDE_VISUAL_STUDIO.md" -ForegroundColor Gray
    Write-Host ""
    exit 1
}

Write-Host ""
Write-Host "📦 COMPILANDO MICROSERVICIOS" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
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

$compiledCount = 0
$failedCount = 0
$rootPath = Get-Location

foreach ($ms in $microservicios) {
    $msPath = Join-Path $rootPath $ms
    
    if (-not (Test-Path $msPath)) {
        Write-Host "⏭️  $ms - NO ENCONTRADO" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "📦 $ms..." -ForegroundColor Cyan -NoNewline
    
    Push-Location $msPath
    
    # Ejecutar Maven
    if ($mvnPath -eq "mvn") {
        mvn clean install -DskipTests -q 2>&1 | Out-Null
    } else {
        & $mvnPath clean install -DskipTests -q 2>&1 | Out-Null
    }
    
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
Write-Host "✅ Compilados: $compiledCount" -ForegroundColor Green
Write-Host "❌ Errores: $failedCount" -ForegroundColor $(if ($failedCount -gt 0) { "Red" } else { "Green" })
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

if ($failedCount -eq 0) {
    Write-Host "✅ ¡COMPILACIÓN EXITOSA!" -ForegroundColor Green
    Write-Host ""
    Write-Host "🚀 PRÓXIMO PASO:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   docker-compose up -d" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host "⚠️  Hay $failedCount error(s) de compilación" -ForegroundColor Red
    exit 1
}
