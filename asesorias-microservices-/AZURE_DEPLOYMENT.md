# Despliegue en Azure (Backend Microservicios)

Esta guía te lleva desde cero hasta tener todos los microservicios corriendo en **Azure Container Apps** con imágenes en **Azure Container Registry (ACR)** y base de datos **Azure Database for PostgreSQL Flexible Server**.

---
## 1. Prerrequisitos locales
- Instalar Azure CLI: https://learn.microsoft.com/es-es/cli/azure/install-azure-cli
- Instalar extensión de Container Apps: `az extension add --name containerapp`
- Tener una suscripción de Azure activa.
- (Opcional) GitHub repo público/privado para CI/CD.

Inicia sesión:
```powershell
az login
```

Selecciona suscripción (si hay varias):
```powershell
az account set --subscription "<SUBSCRIPTION_ID>"
```

---
## 2. Variables base (ajusta nombres únicos)
```powershell
$RG="rg-asesorias"
$LOC="eastus"
$ACR="acrasesorias"          # Debe ser único globalmente
$PG="pg-asesorias"           # Nombre del servidor Postgres
$PG_ADMIN_USER="pgadmin"     # Usuario administrador
$PG_ADMIN_PASS="Cambiar_Esta_Password_123!"  # Password fuerte
$CA_ENV="env-asesorias"      # Entorno de Container Apps
```

---
## 3. Crear Resource Group y ACR
```powershell
az group create -n $RG -l $LOC
az acr create -n $ACR -g $RG --sku Basic --admin-enabled true
```
Obtén credenciales (solo si necesitas usuario/password; para CI usaremos OIDC):
```powershell
az acr credential show -n $ACR
```

---
## 4. Crear servidor PostgreSQL Flexible
```powershell
az postgres flexible-server create ^
  --resource-group $RG ^
  --name $PG ^
  --admin-user $PG_ADMIN_USER ^
  --admin-password $PG_ADMIN_PASS ^
  --tier Burstable --sku-name B1MS --storage-size 32 ^
  --location $LOC ^
  --version 16
```

Abrir firewall para Container Apps (simplificado: permitir acceso público mientras pruebas):
```powershell
az postgres flexible-server firewall-rule create ^
  --resource-group $RG ^
  --name $PG ^
  --rule-name AllowAllAzureIPs ^
  --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255
```

Crear la base y esquema mediante script local (usa psql) o una conexión cliente; el `init.sql` ya provee roles y schemas (puedes aplicarlo desde tu máquina):
```powershell
# Instala psql si no lo tienes y luego:
psql "host=$PG.postgres.database.azure.com port=5432 dbname=postgres user=$PG_ADMIN_USER password=$PG_ADMIN_PASS sslmode=require" -f ./init.sql
```

> Nota: En Azure Flexible Server el host completo es `<nombre>.postgres.database.azure.com`.

---
## 5. Crear entorno de Container Apps
```powershell
az containerapp env create ^
  --name $CA_ENV ^
  --resource-group $RG ^
  --location $LOC
```

---
## 6. Construir y subir imágenes (manual rápido)
Desde carpeta `asesorias-microservices-` (ya tienes los JARs construidos):
```powershell
$REG="$ACR.azurecr.io"
az acr login -n $ACR

$apps = @(
  @{ name="eureka-server"; port=8761 },
  @{ name="api-gateway"; port=8000 },
  @{ name="ms-auth"; port=8088 },
  @{ name="ms-admin"; port=8091 },
  @{ name="ms-asesorias"; port=8082 },
  @{ name="ms-coordinadores"; port=8083 },
  @{ name="ms-divisiones"; port=8084 },
  @{ name="ms-profesores"; port=8085 },
  @{ name="ms-alumnos"; port=8086 }
)

foreach ($a in $apps) {
  docker build -t "$REG/${($a.name)}:v1" -t "$REG/${($a.name)}:latest" ./$($a.name)
  docker push "$REG/${($a.name)}:v1"
  docker push "$REG/${($a.name)}:latest"
}
```

---
## 7. Desplegar Container Apps
Host Postgres (para las variables):
```
PG_HOST=$PG.postgres.database.azure.com
DB_NAME=asesoriasdb
```

Primero Eureka (acceso interno):
```powershell
az containerapp create ^
  --name eureka-server ^
  --resource-group $RG ^
  --environment $CA_ENV ^
  --image $REG/eureka-server:latest ^
  --target-port 8761 ^
  --ingress external ^
  --registry-server $REG --query "properties.configuration.ingress.fqdn"
```
Anota el FQDN (lo llamaremos `EUREKA_FQDN`).

Desplegar los demás (ejemplo ms-admin):
```powershell
az containerapp create ^
  --name ms-admin ^
  --resource-group $RG ^
  --environment $CA_ENV ^
  --image $REG/ms-admin:latest ^
  --target-port 8091 ^
  --ingress internal ^
  --env-vars ^
    EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=https://$EUREKA_FQDN/eureka/ ^
    DB_HOST=$PG_HOST DB_PORT=5432 DB_NAME=$DB_NAME DB_USER=admin DB_PASSWORD=admin_pass ^
  --registry-server $REG
```
Repite para cada microservicio cambiando puerto y DB_USER/DB_PASSWORD.

Gateway (apertura externa):
```powershell
az containerapp create ^
  --name api-gateway ^
  --resource-group $RG ^
  --environment $CA_ENV ^
  --image $REG/api-gateway:latest ^
  --target-port 8000 ^
  --ingress external ^
  --env-vars EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE=https://$EUREKA_FQDN/eureka/ ^
  --registry-server $REG
```
Obtén dominio del gateway:
```powershell
az containerapp show -n api-gateway -g $RG --query properties.configuration.ingress.fqdn -o tsv
```
Usa ese dominio como `VITE_API_BASE_URL` en el frontend.

---
## 8. Frontend (Vercel o Azure Static Web Apps)
En `asesorias-frontend` configura `.env.production`:
```
VITE_API_BASE_URL=https://<gateway-fqdn>
```
Build local:
```powershell
cd asesorias-frontend
npm ci
npm run build
```
Sube repo a GitHub y en Vercel crea proyecto, agrega la variable de entorno. Alternativa Azure Static Web Apps:
```powershell
az staticwebapp create -n swa-asesorias -g $RG -l $LOC --source https://github.com/113012DavidT/front_asesorias --branch main --app-location . --output-location dist
```
Define variable en portal (Configuration) `VITE_API_BASE_URL`.

---
## 9. CORS (Gateway)
Asegúrate de tener configuración que permita el dominio del frontend en el Gateway (`application.yml` o filtro CORS global). Añade el dominio de Vercel o Static Web App.

---
## 10. CI/CD (Resumen)
- GitHub Actions: construye y sube imágenes al ACR.
- Luego usa `az containerapp update` para renovar imagen (o habilita autodeploy si usas etiquetas).

Ejemplo update:
```powershell
az containerapp update -n ms-admin -g $RG --image $REG/ms-admin:latest
```

---
## 11. Monitoreo
Activa Log Analytics al crear el entorno (opcional) para trazas y métricas.

---
## 12. Hardening (Producción)
- Rotar JWT secret y guardarlo en secrets de Container Apps.
- Usar `sslmode=require` en la URL Postgres (ya forzado por Azure).
- Migrar a Flyway y cambiar `ddl-auto` a `validate`.
- Escalar réplicas críticas (`az containerapp revision set-mode manual` si necesitas control de revisiones).

---
## 13. Limpieza
```powershell
az group delete -n $RG --yes --no-wait
```

---
## 14. Siguientes pasos
- Agregar pipeline para frontend
- Integrar Health Checks y alerta de caída

---
## 15. Variables por microservicio (resumen rápido)
| Servicio        | Puerto | Schema       | DB_USER        |
|-----------------|--------|--------------|----------------|
| ms-asesorias    | 8082   | asesorias    | asesorias      |
| ms-admin        | 8091   | admin        | admin          |
| ms-profesores   | 8085   | profesores   | profesores     |
| ms-alumnos      | 8086   | alumnos      | alumnos        |
| ms-coordinadores| 8083   | coordinadores| coordinadores  |
| ms-divisiones   | 8084   | divisiones   | divisiones     |
| ms-auth         | 8088   | (sin DB)     | (N/A)          |
| eureka-server   | 8761   | (sin DB)     | (N/A)          |
| api-gateway     | 8000   | (sin DB)     | (N/A)          |

---
## 16. Problemas comunes
- Eureka no registra: revisar `EUREKA_CLIENT_SERVICE_URL_DEFAULT_ZONE` y conectividad.
- Timeout DB: firewall Postgres no tiene tu rango IP / Container Apps.
- CORS: frontend bloqueado -> configurar orígenes permitidos.
- Imágenes antiguas: ejecutar `az containerapp update --image ...` tras push.

---
Listo. Ajusta nombres y contraseñas antes de producción.
