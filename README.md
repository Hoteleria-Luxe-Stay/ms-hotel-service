# Hotel Service - Sistema de Reservas de Hoteles

Microservicio de gestión de hoteles, habitaciones, departamentos y tipos de habitación. Proporciona información sobre disponibilidad y precios.

## Información del Servicio

| Propiedad | Valor |
|-----------|-------|
| Puerto | 8082 |
| Java | 21 |
| Spring Boot | 3.5.7 |
| Spring Cloud | 2024.0.1 |
| Context Path | /api/v1 |
| Base de Datos | MySQL |

## Estructura del Proyecto

```
ms-hotel/
└── hotel-service/
    ├── pom.xml
    ├── Dockerfile
    ├── contracts/
    │   └── hotel-service-api.yaml
    └── src/main/
        ├── java/com/hotel/hotel/
        │   ├── HotelServiceApplication.java
        │   ├── api/
        │   │   ├── DepartamentosController.java
        │   │   ├── HotelesController.java
        │   │   ├── HabitacionesController.java
        │   │   └── TiposHabitacionController.java
        │   ├── core/
        │   │   ├── departamento/ (model, repository, service)
        │   │   ├── hotel/ (model, repository, service)
        │   │   ├── habitacion/ (model, repository, service)
        │   │   └── tipoHabitacion/ (model, repository, service)
        │   ├── helpers/ (exceptions, mappers)
        │   ├── infrastructure/ (config, security)
        │   └── internal/ (AuthInternalApi, DTOs)
        └── resources/
            └── application.yml
```

## Endpoints

### Departamentos

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/departamentos` | Listar todos | No |
| GET | `/api/v1/departamentos/{id}` | Obtener por ID | No |
| POST | `/api/v1/departamentos` | Crear | ADMIN |

### Hoteles

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/hoteles` | Listar (filtro por departamentoId) | No |
| GET | `/api/v1/hoteles/{id}` | Obtener detalle con habitaciones | No |
| POST | `/api/v1/hoteles` | Crear hotel | ADMIN |
| PUT | `/api/v1/hoteles/{id}` | Actualizar hotel | ADMIN |
| DELETE | `/api/v1/hoteles/{id}` | Eliminar hotel | ADMIN |

### Habitaciones

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/habitaciones` | Listar disponibles (hotelId, fechas) | No |
| GET | `/api/v1/habitaciones/{id}` | Obtener por ID | No |
| GET | `/api/v1/habitaciones/{id}/disponibilidad` | Verificar disponibilidad | No |
| GET | `/api/v1/hoteles/{id}/habitaciones` | Habitaciones de un hotel | No |
| POST | `/api/v1/habitaciones` | Crear habitación | ADMIN |
| PUT | `/api/v1/habitaciones/{id}` | Actualizar | ADMIN |
| DELETE | `/api/v1/habitaciones/{id}` | Eliminar | ADMIN |

### Tipos de Habitación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/tipos-habitacion` | Listar todos | No |

## Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SERVER_PORT` | Puerto del servicio | `8082` |
| `SPRING_DATASOURCE_URL` | URL MySQL | `jdbc:mysql://mysql:3306/hotel_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario BD | `hotel_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña BD | `hotel_pass` |
| `SPRING_RABBITMQ_HOST` | Host RabbitMQ | `rabbitmq` |
| `SPRING_RABBITMQ_PORT` | Puerto RabbitMQ | `5672` |
| `EUREKA_URL` | URL Eureka | `http://discovery-service:8761/eureka` |
| `CONFIG_SERVER_URL` | URL Config Server | `http://config-server:8888` |
| `AUTH_SERVICE_URL` | URL Auth Service | `http://auth-service:8081` |

---

## Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
COPY contracts ./contracts

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/hotel-service-*.jar app.jar

EXPOSE 8082

ENV JAVA_OPTS="-Xms256m -Xmx512m"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  hotel-service:
    build:
      context: ./hotel-service
      dockerfile: Dockerfile
    container_name: hotel-service
    ports:
      - "8082:8082"
    environment:
      - SERVER_PORT=8082
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      - SPRING_DATASOURCE_USERNAME=hotel_user
      - SPRING_DATASOURCE_PASSWORD=hotel_pass
      - SPRING_RABBITMQ_HOST=rabbitmq
      - SPRING_RABBITMQ_PORT=5672
      - EUREKA_URL=http://discovery-service:8761/eureka
      - CONFIG_SERVER_URL=http://config-server:8888
      - AUTH_SERVICE_URL=http://auth-service:8081
    depends_on:
      mysql:
        condition: service_healthy
      auth-service:
        condition: service_healthy
    networks:
      - hotel-network
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8082/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 90s
    restart: unless-stopped

networks:
  hotel-network:
    external: true
```

### Comandos Docker

```bash
# Compilar
cd hotel-service
./mvnw clean package -DskipTests

# Construir imagen
docker build -t hotel-service:latest ./hotel-service

# Ejecutar
docker run -d \
  --name hotel-service \
  -p 8082:8082 \
  -e SERVER_PORT=8082 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/hotel_db \
  -e SPRING_DATASOURCE_USERNAME=hotel_user \
  -e SPRING_DATASOURCE_PASSWORD=hotel_pass \
  -e AUTH_SERVICE_URL=http://auth-service:8081 \
  -e EUREKA_URL=http://discovery-service:8761/eureka \
  --network hotel-network \
  hotel-service:latest

# Verificar
curl http://localhost:8082/actuator/health

# Listar hoteles
curl http://localhost:8082/api/v1/hoteles
```

---

## Kubernetes

### Deployment

```yaml
# k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hotel-service
  namespace: hotel-system
  labels:
    app: hotel-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: hotel-service
  template:
    metadata:
      labels:
        app: hotel-service
    spec:
      containers:
        - name: hotel-service
          image: ${ACR_NAME}.azurecr.io/hotel-service:latest
          ports:
            - containerPort: 8082
          env:
            - name: SERVER_PORT
              value: "8082"
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:mysql://mysql:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: hotel-secrets
                  key: mysql-user
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: hotel-secrets
                  key: mysql-password
            - name: SPRING_RABBITMQ_HOST
              value: "rabbitmq"
            - name: EUREKA_URL
              value: "http://discovery-service:8761/eureka"
            - name: CONFIG_SERVER_URL
              value: "http://config-server:8888"
            - name: AUTH_SERVICE_URL
              value: "http://auth-service:8081"
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8082
            initialDelaySeconds: 90
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8082
            initialDelaySeconds: 60
            periodSeconds: 5
```

### Service

```yaml
# k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: hotel-service
  namespace: hotel-system
spec:
  type: ClusterIP
  selector:
    app: hotel-service
  ports:
    - port: 8082
      targetPort: 8082
      name: http
```

### Comandos Kubernetes

```bash
# Aplicar manifiestos
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Verificar
kubectl get pods -n hotel-system -l app=hotel-service
kubectl logs -f deployment/hotel-service -n hotel-system

# Port-forward
kubectl port-forward svc/hotel-service 8082:8082 -n hotel-system

# Test
curl http://localhost:8082/api/v1/hoteles
curl http://localhost:8082/api/v1/departamentos
```

---

## Azure

### 1. Construir y Subir a ACR

```bash
export ACR_NAME="acrhotelreservas"

az acr login --name $ACR_NAME

az acr build \
  --registry $ACR_NAME \
  --image hotel-service:v1.0.0 \
  --image hotel-service:latest \
  ./hotel-service
```

### 2. Deployment en AKS

```yaml
# k8s/azure-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hotel-service
  namespace: hotel-system
spec:
  replicas: 2
  selector:
    matchLabels:
      app: hotel-service
  template:
    metadata:
      labels:
        app: hotel-service
    spec:
      containers:
        - name: hotel-service
          image: acrhotelreservas.azurecr.io/hotel-service:v1.0.0
          ports:
            - containerPort: 8082
          env:
            - name: SERVER_PORT
              value: "8082"
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:mysql://mysql-hotel-reservas.mysql.database.azure.com:3306/hotel_db?useSSL=true"
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: hotel-secrets
                  key: mysql-user
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: hotel-secrets
                  key: mysql-password
            - name: EUREKA_URL
              value: "http://discovery-service:8761/eureka"
            - name: AUTH_SERVICE_URL
              value: "http://auth-service:8081"
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "500m"
```

### 3. Azure DevOps Pipeline

```yaml
# azure-pipelines.yml
trigger:
  branches:
    include:
      - main
  paths:
    include:
      - ms-hotel/**

variables:
  dockerRegistryServiceConnection: 'acr-connection'
  imageRepository: 'hotel-service'
  containerRegistry: 'acrhotelreservas.azurecr.io'
  dockerfilePath: 'ms-hotel/hotel-service/Dockerfile'
  tag: '$(Build.BuildId)'

pool:
  vmImage: 'ubuntu-latest'

stages:
  - stage: Build
    jobs:
      - job: Build
        steps:
          - task: Maven@3
            displayName: 'Maven Package'
            inputs:
              mavenPomFile: 'ms-hotel/hotel-service/pom.xml'
              goals: 'clean package'
              options: '-DskipTests'
              javaHomeOption: 'JDKVersion'
              jdkVersionOption: '1.21'

          - task: Docker@2
            displayName: 'Build and Push'
            inputs:
              command: buildAndPush
              repository: $(imageRepository)
              dockerfile: $(dockerfilePath)
              containerRegistry: $(dockerRegistryServiceConnection)
              tags: |
                $(tag)
                latest

  - stage: Deploy
    dependsOn: Build
    jobs:
      - deployment: Deploy
        environment: 'production'
        strategy:
          runOnce:
            deploy:
              steps:
                - task: KubernetesManifest@0
                  inputs:
                    action: deploy
                    kubernetesServiceConnection: 'aks-connection'
                    namespace: hotel-system
                    manifests: |
                      ms-hotel/k8s/*.yaml
                    containers: |
                      $(containerRegistry)/$(imageRepository):$(tag)
```

---

## Datos Iniciales

Al iniciar, `DataInit` crea datos de prueba:

### Tipos de Habitación
| Nombre | Capacidad |
|--------|-----------|
| Simple | 1 |
| Doble | 2 |
| Suite | 4 |
| Familiar | 5 |

### Departamentos
| Nombre | Descripción |
|--------|-------------|
| Lima | Costa central del Perú |
| Cusco | Capital histórica |
| Arequipa | Ciudad Blanca |

### Hoteles y Habitaciones
- **Hotel Lima Centro** (5 habitaciones, $80-$250)
- **Hotel Cusco Imperial** (4 habitaciones, $90-$300)
- **Hotel Arequipa Plaza** (4 habitaciones, $70-$220)

---

## Modelo de Datos

```
┌─────────────────┐       ┌─────────────────┐
│  Departamento   │       │ TipoHabitacion  │
├─────────────────┤       ├─────────────────┤
│ id              │       │ id              │
│ nombre          │       │ nombre          │
│ detalle         │       │ descripcion     │
└────────┬────────┘       │ capacidad       │
         │                └────────┬────────┘
         │ 1:N                     │ 1:N
         ▼                         ▼
┌─────────────────┐       ┌─────────────────┐
│     Hotel       │ 1:N   │   Habitacion    │
├─────────────────┼──────►├─────────────────┤
│ id              │       │ id              │
│ nombre          │       │ numero          │
│ direccion       │       │ estado          │
│ descripcion     │       │ precio          │
│ telefono        │       │ capacidad       │
│ email           │       │ tipoHabitacion  │
│ imagenUrl       │       │ hotel           │
│ departamento    │       └─────────────────┘
└─────────────────┘
```

---

## Troubleshooting

```bash
# Ver logs
kubectl logs -f deployment/hotel-service -n hotel-system

# Verificar conexión con auth-service
kubectl exec -it deployment/hotel-service -n hotel-system -- \
  wget -qO- http://auth-service:8081/api/v1/actuator/health

# Verificar BD
kubectl exec -it deployment/hotel-service -n hotel-system -- \
  wget -qO- http://localhost:8082/actuator/health

# Debug endpoints
curl http://localhost:8082/api/v1/hoteles
curl http://localhost:8082/api/v1/habitaciones?hotelId=1
```

---

## Ejecución Local

```bash
cd hotel-service

# Compilar
./mvnw clean package -DskipTests

# Ejecutar
java -jar target/hotel-service-1.0.0-SNAPSHOT.jar \
  --server.port=8082 \
  --spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db

# Swagger UI
open http://localhost:8082/swagger-ui.html
```
