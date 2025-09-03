# METAR Service

A Spring Boot service that continuously fetches METAR weather data for subscribed airports, stores it in a database, and provides REST endpoints for clients to retrieve the data.

---

## Features

- Subscribe/unsubscribe airports
- Store METAR data for airports
- Automatic scheduled fetching of METAR data
- Support for filtering and partial METAR retrieval
- Decode METAR data to human-readable format
- H2 in-memory database for local development
- PostgreSQL for Docker deployment

---

## Requirements

- Docker Desktop (Mac, Windows, Linux)
- Java 17+ (for local runs)
- Maven (for local builds)

---

## Profiles

| Profile  | Database          | Notes                        |
|----------|-----------------|-------------------------------|
| dev      | H2 in-memory     | Local development and testing |
| docker   | PostgreSQL       | Used when running via Docker  |

---

## Running with Docker

### 1. Build and start containers

From the project root:

```bash
  docker compose up --build
```

Starts two services: db (Postgres) and app (Spring Boot service)

Uses the docker Spring profile automatically

### 2. Stop containers

```bash
  docker compose down
```

Stops and removes containers

Postgres data is persisted in a Docker volume (db-data)

### 3. Accessing the service

```bash
  REST API: http://localhost:8080
```

PostgreSQL: localhost:5432
(username: metar, password: metar, database: metar_db)

Running Locally (H2)

```bash
  mvn spring-boot:run
```

Uses the dev profile by default

Runs with H2 in-memory database

Scheduler fetches METAR data for subscribed airports

# API Endpoints

## Subscribe an airport

| Method   | Endpoint                | Description                                          | Request Body                          |
| -------- | ----------------------- | ---------------------------------------------------- | ------------------------------------- |
| `POST`   | `/subscriptions`        | Subscribe an airport                                 | `{ "icaoCode": "LDZA" }`              |
| `GET`    | `/subscriptions`        | List all subscriptions (supports optional filtering) | Query params: `active=true&prefix=LD` |
| `PUT`    | `/subscriptions/{icao}` | Activate or deactivate a subscription                | `{ "active": true }`                  |
| `DELETE` | `/subscriptions/{icao}` | Unsubscribe an airport                               | None                                  |

## Metar data

| Method | Endpoint                        | Description                            | Query Params / Notes                                      |
| ------ | ------------------------------- | -------------------------------------- | --------------------------------------------------------- |
| `POST` | `/airport/{icao}/METAR`         | Store METAR data for an airport        | Body: `{ "data": "<raw METAR string>" }`                  |
| `GET`  | `/airport/{icao}/METAR`         | Retrieve the latest METAR              | Optional: `fields=wind,temperature` to fetch partial data |
| `GET`  | `/airport/{icao}/METAR/decoded` | Retrieve decoded, human-readable METAR | None                                                      |

## Get subscriptions

### Only active subscriptions

```
  GET /subscriptions?active=true
```

### Filter by ICAO prefix

```
  GET /subscriptions?prefix=LD
```

### Filter by active and prefix

```
  GET /subscriptions?active=true&prefix=LD
```

# Swagger / API Documentation

The METAR service provides interactive API documentation using Swagger (Springdoc OpenAPI).

## 1. Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui/index.html
```
- Browse all available endpoints
- View request/response models
- Test API calls directly from the browser

## 2. Access OpenAPI JSON

The full OpenAPI specification in JSON format is available at:
```
http://localhost:8080/v3/api-docs
```
- Can be used to generate API clients or for automated tools
- Works with Postman, ReDoc, or other OpenAPI-compatible tools

## 3. Additional Information

- Ensure the Spring Boot application is running before accessing Swagger UI or OpenAPI JSON.
- DTOs and controllers are annotated with @Schema, @Operation, and @Tag to provide detailed API documentation.
- If you are using a custom context path, prepend it to the URL, e.g.:
```
http://localhost:8080/api/swagger-ui/index.html
```
##### Notes

METAR data is parsed into separate fields: observedAt, wind, temperature, visibility

Partial retrieval is supported via query parameters

Scheduler interval and METAR base URL can be configured in application-docker.properties or application-dev.properties

Local development uses H2, Docker uses PostgreSQL automatically