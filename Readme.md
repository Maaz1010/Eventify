# Eventify

Event ticketing REST API built with Spring Boot. Organizers create and publish events, attendees buy tickets and get QR codes, and staff validate tickets at the venue. Access is role-based, using JWTs issued by Keycloak.

## Tech Stack

- Java, Spring Boot, Maven
- Spring Security (OAuth2 Resource Server, JWT)
- PostgreSQL, Spring Data JPA, Hibernate
-  Lombok
- springdoc-openapi (Swagger UI)
- Docker / Docker Compose

## Key Dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | REST API |
| `spring-boot-starter-data-jpa` | Persistence with Hibernate |
| `spring-boot-starter-security` | Security filter chain |
| `spring-boot-starter-oauth2-resource-server` | JWT validation |
| `spring-boot-starter-validation` | Request validation |
| `postgresql` | PostgreSQL driver |
| `mapstruct` | Entity and DTO mapping |
| `lombok` | Boilerplate reduction |
| `com.google.zxing:core`, `javase` | QR code generation |
| `springdoc-openapi-starter-webmvc-ui` | Swagger UI |
| `spring-boot-docker-compose` | Starts the Docker Compose services when the app runs |

## Authentication and Roles

Login is handled by Keycloak (realm `event-ticket-platform`). The API runs as a resource server and validates the JWT on every request. Roles are read from the token and enforced in the security filter chain. A filter creates the user record in the database on first request.

| Role | Can do |
|---|---|
| `ORGANIZER` | Create, update, list and delete own events and ticket types |
| `ATTENDEE` | Buy tickets, view own tickets, get ticket QR codes |
| `STAFF` | Validate tickets (manually or by QR code) |

#### Published events can be browsed without logging in.

For local testing there is a `dev`-profile-only endpoint (`POST /jwt/token`) that fetches a Keycloak token. It is not created in any other profile.

## Docker

PostgreSQL and Keycloak run through Docker Compose. With `spring-boot-docker-compose` on the classpath, running the app with the `dev` profile starts the containers automatically. Swagger UI is at `/swagger-ui.html`.

## Endpoints

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/v1/events` | ORGANIZER | Create event with ticket types |
| GET | `/api/v1/events` | ORGANIZER | List own events (paged) |
| GET | `/api/v1/events/{eventId}` | ORGANIZER | Get own event details |
| PUT | `/api/v1/events/{eventId}` | ORGANIZER | Update own event |
| DELETE | `/api/v1/events/{eventId}` | ORGANIZER | Delete own event |
| GET | `/api/v1/published-events?q=` | Public | List or search published events |
| GET | `/api/v1/published-events/{eventId}` | Public | Published event details |
| POST | `/api/v1/tickets/{ticketTypeId}` | ATTENDEE | Purchase a ticket |
| GET | `/api/v1/tickets` | ATTENDEE | List own tickets (paged) |
| GET | `/api/v1/tickets/{ticketId}` | ATTENDEE | Get own ticket |
| GET | `/api/v1/tickets/{ticketId}/qr-codes` | ATTENDEE | Get ticket QR code (PNG) |
| POST | `/api/v1/ticket-validations` | STAFF | Validate a ticket (manual or QR) |

## Project Structure

```
src/main/java/com/tickets/eventify
├── config         # Security and OpenAPI configuration
├── controllers    # REST controllers
├── domain
│   ├── dtos       # Request and response DTOs
│   └── entities   # JPA entities
│  
├── exceptions     # Custom exceptions
├── filters        # User provisioning filter
├── mappers        # MapStruct mappers
├── repositories   # Spring Data JPA repositories
├── services       # Business logic
└── util           # Helpers
```



