# 🐳 Docker Configuration - Sistema de Asesorías Frontend

## Dockerfile

```dockerfile
# Frontend build stage
FROM node:18-alpine as builder

WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias
RUN npm ci

# Copiar código fuente
COPY . .

# Build
RUN npm run build

# Production stage
FROM node:18-alpine

WORKDIR /app

# Instalar servidor HTTP simple
RUN npm install -g serve

# Copiar build desde stage anterior
COPY --from=builder /app/dist ./dist

# Exponer puerto
EXPOSE 3000

# Comando para iniciar
CMD ["serve", "-s", "dist", "-l", "3000"]
```

## .dockerignore

```
node_modules
npm-debug.log
.git
.gitignore
README.md
ARQUITECTURA.md
DESARROLLO.md
CHECKLIST.md
.vscode
.env
dist
```

## docker-compose.yml

```yaml
version: '3.8'

services:
  frontend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      - REACT_APP_API_URL=http://backend:8000
    depends_on:
      - backend
    networks:
      - asesorias-network

  backend:
    image: backend:latest  # Reemplazar con imagen real
    ports:
      - "8000:8000"
    networks:
      - asesorias-network
    environment:
      - JAVA_OPTS=-Xmx512m

networks:
  asesorias-network:
    driver: bridge
```

## Construcción de Imagen

```bash
# Construir imagen
docker build -t asesorias-frontend:1.0.0 .

# Ejecutar contenedor
docker run -p 3000:3000 \
  -e VITE_API_URL=http://localhost:8000 \
  asesorias-frontend:1.0.0

# Con docker-compose
docker-compose up -d
```

## Configuración en Producción

### Variables de Entorno

```yaml
environment:
  - VITE_API_URL=https://api.produccion.com
  - VITE_APP_NAME="Sistema de Asesorías"
  - VITE_ENABLE_NOTIFICATIONS=true
```

### Nginx Reverse Proxy

```nginx
server {
    listen 80;
    server_name asesorias.ejemplo.com;

    location / {
        proxy_pass http://frontend:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Cache de activos estáticos
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

## Comandos Útiles

```bash
# Listar imágenes
docker images

# Listar contenedores
docker ps

# Ver logs
docker logs -f <container-id>

# Parar contenedor
docker stop <container-id>

# Remover imagen
docker rmi asesorias-frontend:1.0.0

# Push a registry
docker tag asesorias-frontend:1.0.0 tu-usuario/asesorias-frontend:1.0.0
docker push tu-usuario/asesorias-frontend:1.0.0
```

## Multiarch Build

```bash
# Buildx para múltiples arquitecturas
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t tu-usuario/asesorias-frontend:1.0.0 \
  --push \
  .
```

## Health Check

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:3000 || exit 1
```

---

**Nota**: Estos archivos están listos para ser implementados cuando se integre con Docker.
