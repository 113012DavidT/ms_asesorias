# Quick Start Script para Windows - Sistema de Asesorías Frontend

Write-Host "🚀 Iniciando Sistema de Asesorías Frontend" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""

# Verificar si Node está instalado
if (-not (Get-Command node -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Node.js no está instalado" -ForegroundColor Red
    Write-Host "📦 Descargar desde: https://nodejs.org" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Node.js versión: $(node -v)" -ForegroundColor Green
Write-Host "✅ npm versión: $(npm -v)" -ForegroundColor Green
Write-Host ""

# Instalar dependencias si node_modules no existe
if (-not (Test-Path "node_modules")) {
    Write-Host "📦 Instalando dependencias..." -ForegroundColor Yellow
    npm install
    Write-Host "✅ Dependencias instaladas" -ForegroundColor Green
} else {
    Write-Host "✅ Dependencias ya instaladas" -ForegroundColor Green
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Green
Write-Host "🌐 Frontend iniciará en: http://localhost:5173" -ForegroundColor Cyan
Write-Host "📡 API esperada en: http://localhost:8000" -ForegroundColor Cyan
Write-Host ""
Write-Host "Credenciales de prueba:" -ForegroundColor Yellow
Write-Host "  Admin:        admin@uteq.edu / admin123" -ForegroundColor White
Write-Host "  Coordinador:  coordinador1@uteq.edu / pass123" -ForegroundColor White
Write-Host "  Profesor:     profesor1@uteq.edu / pass123" -ForegroundColor White
Write-Host "  Alumno:       alumno1@uteq.edu / pass123" -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""

# Iniciar servidor de desarrollo
Write-Host "▶️  Iniciando servidor de desarrollo..." -ForegroundColor Yellow
npm run dev
