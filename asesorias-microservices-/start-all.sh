#!/bin/bash
# Script para levantar todos los servicios localmente sin Docker
# Ejecuta cada microservicio en su propio terminal

echo "=========================================="
echo "Iniciando Sistema de Microservicios"
echo "=========================================="

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Esperar tiempo especificado
wait_for_startup() {
    local seconds=$1
    echo -e "${BLUE}Esperando $seconds segundos...${NC}"
    sleep $seconds
}

# Terminal 1: Eureka Server
echo -e "${GREEN}1. Iniciando Eureka Server en puerto 8761...${NC}"
cd eureka-server && mvn spring-boot:run &
EUREKA_PID=$!
wait_for_startup 10

# Terminal 2: MS-Auth
echo -e "${GREEN}2. Iniciando MS-Auth en puerto 8088...${NC}"
cd ../ms-auth && mvn spring-boot:run &
AUTH_PID=$!
wait_for_startup 8

# Terminal 3: API Gateway
echo -e "${GREEN}3. Iniciando API Gateway en puerto 8000...${NC}"
cd ../api-gateway && mvn spring-boot:run &
GATEWAY_PID=$!
wait_for_startup 8

# Terminal 4: MS-Admin
echo -e "${GREEN}4. Iniciando MS-Admin en puerto 8081...${NC}"
cd ../ms-admin && mvn spring-boot:run &
ADMIN_PID=$!
wait_for_startup 8

# Terminal 5: MS-Asesorías
echo -e "${GREEN}5. Iniciando MS-Asesorías en puerto 8082...${NC}"
cd ../ms-asesorias && mvn spring-boot:run &
ASESORIAS_PID=$!
wait_for_startup 8

# Terminal 6: MS-Coordinadores
echo -e "${GREEN}6. Iniciando MS-Coordinadores en puerto 8083...${NC}"
cd ../ms-coordinadores && mvn spring-boot:run &
COORDINADORES_PID=$!
wait_for_startup 8

# Terminal 7: MS-Divisiones
echo -e "${GREEN}7. Iniciando MS-Divisiones en puerto 8084...${NC}"
cd ../ms-divisiones && mvn spring-boot:run &
DIVISIONES_PID=$!
wait_for_startup 8

# Terminal 8: MS-Profesores
echo -e "${GREEN}8. Iniciando MS-Profesores en puerto 8085...${NC}"
cd ../ms-profesores && mvn spring-boot:run &
PROFESORES_PID=$!
wait_for_startup 8

# Terminal 9: MS-Alumnos
echo -e "${GREEN}9. Iniciando MS-Alumnos en puerto 8086...${NC}"
cd ../ms-alumnos && mvn spring-boot:run &
ALUMNOS_PID=$!
wait_for_startup 5

echo ""
echo "=========================================="
echo -e "${GREEN}✅ TODOS LOS SERVICIOS HAN INICIADO${NC}"
echo "=========================================="
echo ""
echo "URLs disponibles:"
echo "  • Eureka Dashboard: http://localhost:8761"
echo "  • API Gateway: http://localhost:8000"
echo "  • MS-Admin: http://localhost:8081"
echo ""
echo "Para detener, presiona Ctrl+C"
echo ""

# Esperar a que todos terminen
wait
