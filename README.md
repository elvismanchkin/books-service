# Reactive Books Service

A proof-of-concept reactive microservice for managing books, built with modern Java and reactive technologies.

## Technology Stack

- **Java 21**: Latest LTS version with modern language features
- **Micronaut**: Modern, reactive microservices framework
- **PostgreSQL**: Robust relational database
- **R2DBC**: Reactive Relational Database Connectivity
- **RabbitMQ**: Message broker for event-driven architecture
- **Maven**: Build automation and dependency management
- **Lombok**: Reduces boilerplate code
- **MapStruct**: Type-safe bean mapping

## Features

- **Fully Reactive API**: Non-blocking endpoints using Project Reactor
- **Context Propagation**: MDC context propagation across reactive streams
- **Error Handling**: Global exception handling with Zalando Problem
- **Observability**:
    - Structured JSON Logging (switchable to console for local development)
    - Request/Response logging
    - Metrics with Micrometer and Prometheus
    - Distributed Tracing with OpenTelemetry
- **Health Checks**: Kubernetes-compatible liveness and readiness probes
- **Event-Driven**: RabbitMQ integration for publishing domain events
- **Documentation**: OpenAPI specification with Swagger UI

## Getting Started

### Prerequisites

- JDK 21
- Docker and Docker Compose
- Maven

### Local Development

1. Clone the repository:
   ```
   git clone https://github.com/your-org/reactive-books-service.git
   cd reactive-books-service
   ```

2. Start the required infrastructure:
   ```
   docker-compose up -d
   ```

3. Build and run the application:
   ```
   ./mvnw clean package
   java -jar target/reactive-books-service-0.1.0.jar
   ```

4. Access the API at http://localhost:8080/api
    - Swagger UI: http://localhost:8080/api/swagger-ui/

### Environment Variables

| Variable                             | Description                          | Default                                   |
|--------------------------------------|--------------------------------------|-------------------------------------------|
| `LOG_FORMAT`                         | Logging format (`JSON` or `CONSOLE`) | `CONSOLE`                                 |
| `R2DBC_DATASOURCES_DEFAULT_URL`      | Database URL                         | `r2dbc:postgresql://localhost:5432/books` |
| `R2DBC_DATASOURCES_DEFAULT_USERNAME` | Database username                    | `books`                                   |
| `R2DBC_DATASOURCES_DEFAULT_PASSWORD` | Database password                    | `passw0rd`                                |
| `RABBITMQ_URI`                       | RabbitMQ connection URI              | `amqp://books:books@localhost:5672/books` |

## API Documentation

The service provides a RESTful API for managing books and categories:

### Book Operations

- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/isbn/{isbn}` - Get book by ISBN
- `GET /api/books/search?title={title}&author={author}` - Search books by title or author
- `POST /api/books` - Create a new book
- `PUT /api/books/{id}` - Update an existing book
- `DELETE /api/books/{id}` - Delete a book

### Category Operations

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/categories/name/{name}` - Get category by name
- `POST /api/categories` - Create a new category
- `PUT /api/categories/{id}` - Update an existing category
- `DELETE /api/categories/{id}` - Delete a category

## Observability

### Metrics

The service exposes Prometheus metrics at `/prometheus`.

### Tracing

OpenTelemetry tracing is configured to send traces to the collector.

### Logging

The service uses structured logging with contextual information:

- Request ID
- Correlation ID
- User ID (when available)
- Session ID (when available)

## Deployment

### Docker Build

```bash
./mvnw clean package
docker build -t reactive-books-service:0.1.0 .
```

### Kubernetes

Deploy to Kubernetes using the provided manifest:

```bash
kubectl apply -f k8s/deployment.yaml
```

## Custom HTTP Status Codes

The service uses custom HTTP status codes for certain situations:

- `475` - Business validation error (custom code for business rules violations)

## Project Structure

```
reactive-books-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── config/         # Configuration classes
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── domain/         # Domain entities
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── event/          # Event classes
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── filter/         # HTTP filters
│   │   │   ├── health/         # Health checks
│   │   │   ├── interceptor/    # Interceptors
│   │   │   ├── mapper/         # Object mappers
│   │   │   ├── messaging/      # RabbitMQ producers/consumers
│   │   │   ├── repository/     # Data repositories
│   │   │   └── service/        # Business logic
│   │   └── resources/          # Configuration files
│   └── test/                   # Test classes
├── k8s/                        # Kubernetes manifests
├── config/                     # Config for observability tools
└── docker-compose.yml          # Local development environment
```

## Contributing

Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting
pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Micronaut 4.7.6 Documentation

- [User Guide](https://docs.micronaut.io/4.7.6/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.7.6/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.7.6/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)

## Feature r2dbc documentation

- [Micronaut R2DBC documentation](https://micronaut-projects.github.io/micronaut-r2dbc/latest/guide/)

- [https://r2dbc.io](https://r2dbc.io)

## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)

## Feature data-r2dbc documentation

- [Micronaut Data R2DBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/#dbc)

- [https://r2dbc.io](https://r2dbc.io)

## Feature problem-json documentation

- [Micronaut Problem JSON documentation](https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/index.html)

## Feature multi-tenancy documentation

- [Micronaut Multitenancy documentation](https://docs.micronaut.io/latest/guide/index.html#multitenancy)

## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)

## Feature reactor documentation

- [Micronaut Reactor documentation](https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)

## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)

## Feature swagger-ui documentation

- [Micronaut Swagger UI documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature lombok documentation

- [Micronaut Project Lombok documentation](https://docs.micronaut.io/latest/guide/index.html#lombok)

- [https://projectlombok.org/features/all](https://projectlombok.org/features/all)


