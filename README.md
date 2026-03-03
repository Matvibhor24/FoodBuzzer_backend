# FoodBuzzer Backend

Spring Boot backend for FoodBuzzer app. Uses MySQL, JPA, and REST APIs for auth.

## Prerequisites

- **Java 17**
- **MySQL** (running on localhost)

## Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE foodbuzzerdb;
   ```

2. Copy the example config and add your DB credentials:
   ```bash
   # Windows (PowerShell)
   Copy-Item src/main/resources/application.properties.example src/main/resources/application.properties

   # Linux / macOS / Git Bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Edit `application.properties` and set `spring.datasource.username` and `spring.datasource.password`.

## Run

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

App runs at: http://localhost:8080

## API Endpoints

- `POST /api/auth/register` – Register a user
- `POST /api/auth/login` – Login
