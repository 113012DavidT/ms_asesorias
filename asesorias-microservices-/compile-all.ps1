#!/usr/bin/env pwsh
# Script para compilar todos los microservicios con Maven

# Buscar Maven en rutas comunes
$mavenPaths = @(
    "C:\Program Files\Maven\bin\mvn.cmd",
    "C:\Program Files (x86)\Maven\bin\mvn.cmd",
    "C:\apache-maven\bin\mvn.cmd",
    "$env:MAVEN_HOME\bin\mvn.cmd",
    "$env:M2_HOME\bin\mvn.cmd"
)

$mvnPath = $null
foreach ($path in $mavenPaths) {
    if (Test-Path $path) {
        $mvnPath = $path
        break
    }
}

if (-not $mvnPath) {
    Write-Host "❌ Maven no encontrado en el sistema" -ForegroundColor Red
    Write-Host ""
    Write-Host "📋 Opciones:" -ForegroundColor Yellow
    Write-Host "1. Instala Maven desde: https://maven.apache.org/download.cgi"
    Write-Host "2. O descarga desde: https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
    Write-Host "3. Extrae en C:\apache-maven"
    Write-Host "4. Y ejecuta este script de nuevo"
    Write-Host ""
    exit 1
}

Write-Host "✅ Maven encontrado en: $mvnPath" -ForegroundColor Green
Write-Host ""

# Array de microservicios
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

Write-Host "🔨 Compilando todos los microservicios..." -ForegroundColor Cyan
Write-Host ""

foreach ($ms in $microservicios) {
    $msPath = Join-Path (Get-Location) $ms
    
    if (-not (Test-Path $msPath)) {
        Write-Host "⚠️  $ms no encontrado" -ForegroundColor Yellow
        continue
    }
    
    Write-Host "📦 Compilando $ms..." -ForegroundColor Cyan
    
    Push-Location $msPath
    & $mvnPath clean install -DskipTests -q
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ $ms compilado exitosamente" -ForegroundColor Green
    } else {
        Write-Host "❌ Error compilando $ms" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    
    Pop-Location
}

Write-Host ""
Write-Host "✅ ¡Todos los microservicios compilados exitosamente!" -ForegroundColor Green
Write-Host ""
Write-Host "🚀 Próximo paso: Ejecuta 'docker-compose up -d'" -ForegroundColor Yellow
