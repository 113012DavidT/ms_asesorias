#!/bin/bash
# Quick Start Script para el Sistema de Asesorías Frontend

echo "🚀 Iniciando Sistema de Asesorías Frontend"
echo "=========================================="
echo ""

# Verificar si Node está instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js no está instalado"
    echo "📦 Descargar desde: https://nodejs.org"
    exit 1
fi

echo "✅ Node.js versión: $(node -v)"
echo "✅ npm versión: $(npm -v)"
echo ""

# Instalar dependencias si node_modules no existe
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependencias..."
    npm install
    echo "✅ Dependencias instaladas"
else
    echo "✅ Dependencias ya instaladas"
fi

echo ""
echo "=========================================="
echo "🌐 Frontend iniciará en: http://localhost:5173"
echo "📡 API esperada en: http://localhost:8000"
echo ""
echo "Credenciales de prueba:"
echo "  Admin:        admin@uteq.edu / admin123"
echo "  Coordinador:  coordinador1@uteq.edu / pass123"
echo "  Profesor:     profesor1@uteq.edu / pass123"
echo "  Alumno:       alumno1@uteq.edu / pass123"
echo "=========================================="
echo ""

# Iniciar servidor de desarrollo
echo "▶️  Iniciando servidor de desarrollo..."
npm run dev
