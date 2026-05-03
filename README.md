# Hotel Service - Sistema de Reserva de Hoteles

Microservicio de gestión de hoteles, departamentos, habitaciones y tipos de habitación. Lectura del catálogo es pública; escritura restringida al rol `ADMIN`. Se autentica contra `auth-service` para obtener tokens técnicos (OAuth2 `client_credentials`).

## Información del Servicio

| Propiedad | Valor |
|-----------|-------|
| Puerto | 8082 |
| Java | 21 |
| Spring Boot | 3.4.0 |
| Spring Cloud | 2024.0.1 |
| Context Path | `/api/v1` |
| Base de Datos | MySQL |
| Validación JWT | **RS256** con clave pública RSA compartida |

## Estructura del Proyecto (Package-by-Feature)

```
ms-hotel-service/
├── pom.xml
├── Dockerfile
├── env.example                ← copialo a .env y completalo en DEV
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
    │   ├── helpers/
    │   │   ├── auth/AuthUtils.java
    │   │   ├── errors/ApiErrorResponse.java
    │   │   ├── exceptions/ (EntityNotFoundException, ValidationException)
    │   │   └── mappers/ (DepartamentoMapper, HotelMapper, HabitacionMapper, TipoHabitacionMapper)
    │   ├── infrastructure/
    │   │   ├── config/ (SecurityConfig, JwtConfig, RestTemplateConfig, GlobalExceptionHandler)
    │   │   └── security/AuthContextFilter.java
    │   └── internal/
    │       ├── AuthInternalApi.java       ← cliente REST hacia auth-service
    │       ├── ServiceTokenProvider.java  ← cachea token OAuth2 c.c.
    │       └── dto/ (TokenValidationResponse, UserInternalResponse)
    └── resources/
        └── application.yml    ← bootstrap mínimo (config-server lo hidrata)
```

## Endpoints

### Departamentos

| Método | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/v1/departamentos` | Público |
| GET | `/api/v1/departamentos/{id}` | Público |
| POST | `/api/v1/departamentos` | ADMIN |

### Hoteles

| Método | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/v1/hoteles` (filtro por `departamentoId`) | Público |
| GET | `/api/v1/hoteles/{id}` | Público |
| POST | `/api/v1/hoteles` | ADMIN |
| PUT | `/api/v1/hoteles/{id}` | ADMIN |
| DELETE | `/api/v1/hoteles/{id}` | ADMIN |

### Habitaciones

| Método | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/v1/habitaciones` (filtros `hotelId`, fechas) | Público |
| GET | `/api/v1/habitaciones/{id}` | Público |
| GET | `/api/v1/habitaciones/{id}/disponibilidad` | Público |
| GET | `/api/v1/hoteles/{id}/habitaciones` | Público |
| POST | `/api/v1/habitaciones` | ADMIN |
| PUT | `/api/v1/habitaciones/{id}` | ADMIN |
| DELETE | `/api/v1/habitaciones/{id}` | ADMIN |

### Tipos de Habitación

| Método | Endpoint | Auth |
|--------|----------|------|
| GET | `/api/v1/tipos-habitacion` | Público |

## Variables de Entorno

| Variable | Obligatoria | Descripción | Ejemplo (DEV) |
|----------|-------------|-------------|---------------|
| `CONFIG_IMPORT` | No | Import de Spring Cloud Config | `optional:configserver:http://localhost:8888` |
| `CONFIG_FAIL_FAST` | No | Falla rápido si config-server no responde | `false` (DEV) / `true` (PROD) |
| `SERVER_PORT` | No | Puerto HTTP (default 8082) | `8082` |
| `EUREKA_URL` | No | URL de Eureka (default `http://discovery-service:8761/eureka`) | `http://localhost:8761/eureka` |
| `SPRING_DATASOURCE_URL` | **Sí** | JDBC URL de MySQL | `jdbc:mysql://localhost:3307/hotel_db` |
| `SPRING_DATASOURCE_USERNAME` | **Sí** | Usuario MySQL | - |
| `SPRING_DATASOURCE_PASSWORD` | **Sí** | Contraseña MySQL | - |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | No | Default `validate` (PROD-safe) | `update` (DEV) |
| `SPRING_JPA_SHOW_SQL` | No | Default `false` (PROD-safe) | `true` (DEV) |
| `JWT_PUBLIC_KEY` | **Sí** | Clave pública RSA del auth-service en PEM (1 línea con `\n`) | - |
| `AUTH_SERVICE_URL` | **Sí** | URL base del auth-service (sin context-path) | `http://localhost:8081` |
| `AUTH_SERVICE_CLIENT_ID` | **Sí** | Client ID asignado a hotel-service en auth-service | - |
| `AUTH_SERVICE_CLIENT_SECRET` | **Sí** | Client secret correspondiente | - |
| `CORS_ALLOWED_ORIGINS` | **Sí** | Origen permitido para CORS | `http://localhost:4200` |

> Las credenciales `AUTH_SERVICE_CLIENT_ID/SECRET` deben coincidir con las que se siembran en `auth-service` mediante `HOTEL_SERVICE_CLIENT_ID/SECRET`.

## Datos Iniciales (al primer arranque)

`DataInit.java` siembra:

- **Tipos de Habitación**: Simple (1), Doble (2), Suite (4), Familiar (5).
- **Departamentos**: Lima, Cusco, Arequipa.
- **Hoteles + habitaciones**:
  - Hotel Lima Centro: 5 habitaciones, USD 80–250
  - Hotel Cusco Imperial: 4 habitaciones, USD 90–300
  - Hotel Arequipa Plaza: 4 habitaciones, USD 70–220

## Seguridad

- **Validación JWT**: RS256 con `JWT_PUBLIC_KEY` (la pareja pública de la clave privada del auth-service).
- **Sesiones**: STATELESS.
- **CORS**: deshabilitado en el servicio (lo maneja el `api-gateway`).
- **Lectura pública**, **escritura restringida** vía claim de rol en el JWT.
- **Service-to-service**: `ServiceTokenProvider` obtiene un token técnico del auth-service vía `client_credentials` y lo cachea hasta 30 segundos antes de la expiración.

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
│ id              │       │ id, numero      │
│ nombre          │       │ estado, precio  │
│ direccion       │       │ capacidad       │
│ ...             │       │ tipoHabitacion  │
└─────────────────┘       │ hotel           │
                          └─────────────────┘
```

## Schema Migrations (Flyway)

El schema está versionado con **Flyway**. Cada cambio = nuevo script en `src/main/resources/db/migration/` con naming `V{n}__descripcion.sql`.

- `V1__init_schema.sql` — estado inicial: `departamento`, `tipo_habitacion`, `hotel`, `habitacion` con FKs e índices.
- Cambios futuros: `V2__...sql`, `V3__...sql`. **NUNCA se edita un script ya aplicado** — siempre se agrega uno nuevo.
- Flyway corre **antes** que Hibernate: aplica los scripts pendientes y luego Hibernate valida (`ddl-auto: validate`) que las entidades calzan con el schema.
- Tabla de control: `flyway_schema_history` (la crea Flyway al arrancar).

### Variables relevantes

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SPRING_FLYWAY_ENABLED` | `true` | Activa/desactiva Flyway |
| `SPRING_FLYWAY_BASELINE_ON_MIGRATE` | `false` | `true` solo si la DB ya tenía tablas pre-Flyway |
| `SPRING_FLYWAY_VALIDATE_ON_MIGRATE` | `true` | Valida checksums de scripts ya aplicados |

### Workflow primera vez

1. Crear el schema vacío en MySQL: `CREATE DATABASE hotel_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. Levantar el servicio → Flyway aplica `V1` automáticamente.
3. Verificar con `SELECT version, description, success FROM flyway_schema_history;`.

## Ejecución Local (DEV)

```bash
# 1. Infra (MySQL + RabbitMQ + Kafka)
docker-compose -f docker-compose.infra.yml up -d

# 2. Variables
cp env.example .env
# editar .env (especialmente JWT_PUBLIC_KEY, AUTH_SERVICE_CLIENT_ID/SECRET y credenciales BD)

# 3. Levantar (auth-service debe estar arriba para los tokens técnicos)
mvn spring-boot:run

# Swagger UI
open http://localhost:8082/api/v1/swagger-ui.html
```

## Ejecución en Docker (PROD)

Multi-stage build, JRE 21 alpine, usuario no-root, healthcheck en `/api/v1/actuator/health`. Se levanta como parte de `docker-compose.prod.yml` (a definir) consumiendo `.env.prod` con TODAS las variables marcadas como obligatorias.

## Troubleshooting

| Síntoma | Causa probable | Solución |
|---------|----------------|----------|
| `No se pudo obtener token tecnico` | auth-service no responde o credenciales inválidas | Verificar `AUTH_SERVICE_URL` y que `AUTH_SERVICE_CLIENT_ID/SECRET` coincida con lo sembrado en auth |
| 401 en endpoints protegidos | `JWT_PUBLIC_KEY` no es pareja de la privada de auth | Regenerar el keypair y propagarlo a auth + gateway + este servicio |
| `Could not resolve placeholder ...` | Falta env var obligatoria | Revisar la tabla de variables |
| Config-server timeouts al iniciar | `CONFIG_FAIL_FAST=true` con config-server caído | DEV: `CONFIG_FAIL_FAST=false`; PROD: levantar config-server primero |
