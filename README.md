# Fraud Microservices with Spring Boot

A Java/Spring Boot microservices demo for customer registration and fraud checks. The project demonstrates service discovery, API routing, synchronous service calls, asynchronous messaging, persistence, and distributed tracing.

> **Demo only:** The Docker Compose configuration contains sample credentials and starts database/admin services. Do not reuse its passwords or expose these services to an untrusted network.

## Services and components

| Component | Purpose | Default port |
| --- | --- | ---: |
| `customer` | Customer registration API and persistence | 8080 |
| `fraud` | Fraud-check API and history | 8081 |
| `notification` | Processes customer notifications from RabbitMQ | 8082 |
| `apigw` | Spring Cloud Gateway entry point | 8083 |
| `eureka-server` | Service discovery | 8761 |
| PostgreSQL | Relational storage for application services | 4040 (host) |
| RabbitMQ | Asynchronous messaging; management UI | 5672; 15672 (management) |
| Zipkin | Distributed trace UI | 9411 |
| pgAdmin | PostgreSQL administration UI | 5050 |

The repository is a Maven multi-module project. Shared clients and AMQP support live in `clients` and `amqp`; deployable services are in `customer`, `fraud`, `notification`, `apigw`, and `eureka-server`.

## Requirements

- Java 17
- Maven 3.8+ (or a Maven wrapper, if available in your checkout)
- Docker and Docker Compose for the containerized demo

## Run with Docker Compose

From the repository root:

```bash
docker compose up -d
```

Compose starts PostgreSQL, pgAdmin, RabbitMQ, Zipkin, service discovery, the gateway, and application services. The application containers use the published `souravDocker1/*:latest` images configured in `docker-compose.yml`.

Check service status and view logs:

```bash
docker compose ps
docker compose logs -f customer
```

Stop the stack:

```bash
docker compose down
```

To remove the persisted database and pgAdmin data as well, use `docker compose down -v`.

## Build

Build all Maven modules from the project root:

```bash
mvn clean package
```

## API

Customer registration is available at `POST /api/v1/customers` on the customer service (port 8080). The request body contains a first name, last name, and email, for example:

```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "email": "ada@example.com"
}
```

The customer service checks the registration with the fraud service and publishes a notification message through RabbitMQ when registration succeeds. The API Gateway is exposed on port 8083; the configured gateway route forwards `/api/v1/customers/**` to the customer service.

## Configuration and security

Service configuration is in each module's `src/main/resources` directory. Docker-specific profiles are provided where applicable. The checked-in Compose and application configuration is for local learning only: replace hard-coded credentials, use secrets or environment-based configuration, and review database schema settings before deploying anywhere beyond a disposable development environment.

## Screenshots

![alt text](./144061535-7a42e85b-59d6-4f7f-9c35-18a48b49e6de%20(1).png)

![Screenshot (2)](https://github.com/user-attachments/assets/158a6b98-0b8a-4955-af1b-f90aedd13979)
