@echo off
setlocal enabledelayedexpansion

set MAVEN_HOME=C:\maven
set PATH=%MAVEN_HOME%\bin;%PATH%

echo === Building ms-admin ===
cd /d "c:\Users\david\Downloads\asesorias-microservices-\ms-admin"
call mvn clean package -DskipTests -q

if %ERRORLEVEL% NEQ 0 (
    echo ERROR building ms-admin
    exit /b 1
)

echo Build complete. Now rebuilding Docker images...

cd /d "c:\Users\david\Downloads\asesorias-microservices-"
docker-compose build --no-cache ms-admin ms-auth
docker-compose rm -f ms-admin ms-auth
docker-compose up -d ms-admin ms-auth

timeout /t 5

echo === Testing login ===
powershell -NoProfile -ExecutionPolicy Bypass -File "test_admin_detailed.ps1"
