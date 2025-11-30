#!/usr/bin/env pwsh
# Script para descargar e instalar Maven automáticamente

$candidateVersions = @("3.9.9","3.9.8","3.9.7","3.9.6")
$downloadSucceeded = $false
$mavenVersion = $null
$mavenZip = $null
$mavenUrl = $null
$mavenHome = "C:\apache-maven"
$mavenBin = Join-Path $mavenHome "bin\mvn.cmd"

Write-Host "🔍 Verificando Maven..." -ForegroundColor Cyan

# Verificar si Maven ya está instalado
if (Test-Path $mavenBin) {
    Write-Host "✅ Maven ya está instalado en $mavenHome" -ForegroundColor Green
    exit 0
}

Write-Host "📥 Intentando descargar Maven (candidatos: $($candidateVersions -join ', '))..." -ForegroundColor Yellow

# Crear directorio si no existe
if (-not (Test-Path $mavenHome)) {
    New-Item -ItemType Directory -Path $mavenHome -Force | Out-Null
}

# Descargar Maven
$downloadPath = Join-Path $env:TEMP $mavenZip
foreach ($ver in $candidateVersions) {
    $mavenVersion = $ver
    $mavenZip = "apache-maven-$mavenVersion-bin.zip"
    $primary = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/$mavenZip"
    $archive = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/$mavenZip"
    $downloadPath = Join-Path $env:TEMP $mavenZip
    Write-Host " → Probando versión $mavenVersion" -ForegroundColor Cyan
    foreach ($url in @($primary,$archive)) {
        Write-Host "   URL: $url" -ForegroundColor Gray
        try {
            Invoke-WebRequest -Uri $url -OutFile $downloadPath -UseBasicParsing -ErrorAction Stop
            Write-Host "   ✅ Descargado $mavenZip" -ForegroundColor Green
            $mavenUrl = $url
            $downloadSucceeded = $true
            break
        } catch {
            Write-Host "   ❌ Falló" -ForegroundColor Yellow
        }
    }
    if ($downloadSucceeded) { break }
}

if (-not $downloadSucceeded) {
    Write-Host "❌ No se pudo descargar Maven en ninguna versión candidata" -ForegroundColor Red
    exit 1
}

Write-Host "📦 Extrayendo Maven..." -ForegroundColor Yellow
try {
    Expand-Archive -Path $downloadPath -DestinationPath $env:TEMP -Force
    $extractedFolder = Join-Path $env:TEMP "apache-maven-$mavenVersion"
    
    # Copiar contenido
    Get-ChildItem -Path $extractedFolder | Copy-Item -Destination $mavenHome -Recurse -Force
    
    Write-Host "✅ Maven instalado en $mavenHome" -ForegroundColor Green
} catch {
    Write-Host "❌ Error extrayendo Maven" -ForegroundColor Red
    Write-Host "   Error: $_" -ForegroundColor Red
    exit 1
} finally {
    # Limpiar archivo temporal
    Remove-Item -Path $downloadPath -Force -ErrorAction SilentlyContinue
}

# Agregar Maven al PATH de esta sesión
$env:Path += ";$mavenHome\bin"

Write-Host ""
Write-Host "✅ Maven listo" -ForegroundColor Green
Write-Host "📍 Ruta: $mavenHome" -ForegroundColor Cyan
